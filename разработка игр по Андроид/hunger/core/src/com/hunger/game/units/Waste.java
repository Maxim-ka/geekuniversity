package com.hunger.game.units;

import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class Waste extends GamePoint{

    public enum Type {

        CORPSE("corpse", 15.0f), THORN("thorn", 10.0f);

        private String textureName;
        private float lifetime;

        Type(String textureName, float lifetime) {
            this.textureName = textureName;
            this.lifetime = lifetime;
        }
    }

    private Type type;

    public Type getType() {
        return type;
    }

    private float time;

    public Waste(GameScreen gs, Type type) {
        super(gs, type.textureName);
        this.type = type;
        satiety = 0.15f;
    }

    public void checkCollision(Eater eater){
        if (eater.getDistance(this) < eater.scale * eater.halfWidth + this.scale * this.halfWidth){
            eater.scale -= satiety;
            if (eater.scale < Rules.MIN_SCALE) eater.active = false;
            this.active = false;
        }
    }

    public void init(GamePoint another){
        this.position.set(another.position);
        scale = (this.getType() == Type.CORPSE) ? 0.5f + another.scale : 0.5f ;
        angle = MathUtils.random(-90.0f, 90.0f);
        active = true;
        time = type.lifetime;
    }

    public void update(float dt) {
        time -= dt;
        if (time <= 0.0f)  {
            this.active = false;
            return;
        }
        super.update(dt);
        angle = (angle > 0)? angle + 90.0f * dt : angle - 90.0f * dt;
    }
}
