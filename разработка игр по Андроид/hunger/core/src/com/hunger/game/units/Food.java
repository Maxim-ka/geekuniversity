package com.hunger.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Food extends GamePoint{

    public enum Type {
        PIZZA(new Texture("pizza_big.png"), 0.05f), LEMON(new Texture("lemon.png"), -0.1f),
        DOUGHNUT(new Texture("doughnut_big.png"), 0.1f);

        private Texture texture;
        private float satiety;

        Type(Texture texture, float satiety) {
            this.texture = texture;
            this.satiety = satiety;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public Food(Type type){
        super(type.texture);
        this.type = type;
        velocity = new Vector2(MathUtils.random(-90.0f, 90.0f), MathUtils.random(-90.0f, 90.0f));
        satiety = type.satiety;
    }

    public void changeTypeFood(){
        type = Food.Type.values()[MathUtils.random(Food.Type.values().length - 1)];
        texture = type.texture;
        satiety = type.satiety;
        velocity.set(MathUtils.random(-90.0f, 90.0f), MathUtils.random(-90.0f, 90.0f));
    }

    public void update(float dt) {
        super.update(dt);
        position.mulAdd(velocity, dt);
        angle = (velocity.x < 0) ? angle + 90.0f * dt : angle - 90.0f * dt;
    }

    public void dispose(){
        for (int i = 0; i < Type.values().length; i++) {
            Type.values()[i].texture.dispose();
        }
    }
}
