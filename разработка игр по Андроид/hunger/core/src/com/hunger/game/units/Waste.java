package com.hunger.game.units;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

import java.io.Serializable;

public class Waste extends GamePoint implements Serializable{

    public enum Type {

        CORPSE(0, 15.0f, -0.25f), THORN(1, 10.0f, -0.25f);

        private int textureIndex;
        private float lifetime;
        private float satiety;

        public int getTextureIndex() {
            return textureIndex;
        }

        Type(int textureIndex, float lifetime, float satiety) {
            this.textureIndex = textureIndex;
            this.lifetime = lifetime;
            this.satiety = satiety;
        }
    }

    private Type type;
    private TextureRegion[] textureRegions;

    public TextureRegion[] getTextureRegions() {
        return textureRegions;
    }

    public Type getType() {
        return type;
    }

    private float time;

    public Waste(GameScreen gs, TextureRegion[] textureRegions) {
        super(gs, null);
        this.textureRegions = textureRegions;
    }

    public void checkCollision(Eater eater){
        if (eater.getDistance(this) < eater.scale * eater.halfWidth + this.scale * this.halfWidth){
            eater.scale += type.satiety;
            if (getType() == Type.CORPSE){
                eater.acceleration = 0.0f;
                scale += type.satiety;
                if (scale < 0){
                    active = false;
                    eater.acceleration = eater.getRandomAcceleration();
                }
            } else active = false;
            if (eater.scale < Rules.MIN_SCALE) eater.active = false;
        }
    }

    public void init(Type  type, GamePoint another){
        if (this.type != type || region == null) {
            this.type = type;
            region = textureRegions[type.textureIndex];
            toSize();
        }
        this.position.set(another.position);
        scale = (this.getType() == Type.CORPSE) ? 1.0f + another.scale : 0.5f ;
        angle = MathUtils.random(-90.0f, 90.0f);
        active = true;
        time = type.lifetime;
    }

    public void update(float dt) {
        time -= dt;
        if (time <= 2.0f && this.getType() == Type.THORN) {
            gs.getParticle().launch(this.position, 32 + MathUtils.random(-8, 16), MathUtils.random(-360, 360), MathUtils.random(1,3), MathUtils.random(0.75f, 1.0f), MathUtils.random(0.15f, 0.25f), 1,0.823f,0,1,0,0,0,0);
        }
        if (time <= 0.0f){
            this.active = false;
            return;
        }
        super.update(dt);
        if (getType() == Type.THORN) angle = (angle > 0)? angle + 90.0f * dt : angle - 90.0f * dt;
    }
}
