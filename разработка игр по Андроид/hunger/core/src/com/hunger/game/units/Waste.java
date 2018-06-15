package com.hunger.game.units;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class Waste extends GamePoint{

    public enum Type {

        CORPSE(0, 15.0f, -0.15f), THORN(1, 10.0f, -0.2f), DETONATION(2, 2.0f, -0.25f);

        private int textureIndex;
        private float lifetime;
        private float satiety;

        Type(int textureIndex, float lifetime, float satiety) {
            this.textureIndex = textureIndex;
            this.lifetime = lifetime;
            this.satiety = satiety;
        }
    }

    private Type type;
    private TextureRegion[] textureRegions;

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
                if (scale < 0.0){
                    active = false;
                    eater.acceleration = eater.getRandomAcceleration();
                }
            } else active = false;
            if (eater.scale < Rules.MIN_SCALE) eater.active = false;
        }
    }

    public void init(Type  type, GamePoint another){
        if (this.type != type) {
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
        if (getType() == Type.DETONATION && time <= 1.0f) scale = 1.0f;
        if (time <= 0.0f){
            if (this.getType() == Type.THORN){
                init(Type.DETONATION, this);
            }else{
                this.active = false;
                return;
            }
        }
        super.update(dt);
        if (getType() == Type.THORN) angle = (angle > 0)? angle + 90.0f * dt : angle - 90.0f * dt;
    }
}
