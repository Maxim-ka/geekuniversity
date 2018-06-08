package com.hunger.game.units;

import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;

public class Food extends GamePoint{

    public enum Type {
        PIZZA("pizza", 0.05f), LEMON("lemon", -0.1f),
        DOUGHNUT("doughnut", 0.1f);

        private String textureName;
        private float satiety;

        Type(String textureName, float satiety) {
            this.textureName = textureName;
            this.satiety = satiety;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public Food(GameScreen gs, Type type){
        super(gs, type.textureName);
        this.type = type;
        satiety = type.satiety;
    }

    public void init(){
        super.init();
        changeTypeFood();
    }

    private void changeTypeFood(){
        type = Food.Type.values()[MathUtils.random(Food.Type.values().length - 1)];
        texture = gs.getAtlas().findRegion(type.textureName);
        satiety = type.satiety;
        velocity.set(MathUtils.random(-90.0f, 90.0f), MathUtils.random(-90.0f, 90.0f));
    }

    public void update(float dt) {
        super.update(dt);
        position.mulAdd(velocity, dt);
        angle = (velocity.x < 0) ? angle + 90.0f * dt : angle - 90.0f * dt;
    }
}
