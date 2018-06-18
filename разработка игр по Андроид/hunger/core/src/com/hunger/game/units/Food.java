package com.hunger.game.units;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;

public class Food extends GamePoint{

    public enum Type {
        PIZZA(0, 0.05f), LEMON(1, -0.1f),
        DOUGHNUT(2, 0.1f);

        private int textureIndex;
        private float satiety;

        Type(int textureIndex, float satiety) {
            this.textureIndex = textureIndex;
            this.satiety = satiety;
        }
    }

    private Type type;
    private TextureRegion[] textureRegions;

    public Type getType() {
        return type;
    }

    public Food(GameScreen gs, TextureRegion[] textureRegions){
        super(gs, null);
        this.textureRegions = textureRegions;
    }

    public void init(Type type){
        super.init();
        if (this.type != type) {
            this.type = type;
            region = textureRegions[type.textureIndex];
            toSize();
        }
        velocity.set(MathUtils.random(-90.0f, 90.0f), MathUtils.random(-90.0f, 90.0f));
        satiety = type.satiety;
    }

    public void update(float dt) {
        super.update(dt);
        position.mulAdd(velocity, dt);
        angle = (velocity.x < 0) ? angle + 90.0f * dt : angle - 90.0f * dt;
    }
}
