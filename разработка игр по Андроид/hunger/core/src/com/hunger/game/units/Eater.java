package com.hunger.game.units;

import com.badlogic.gdx.math.Vector2;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public abstract class Eater extends GamePoint {

    float acceleration;
    float angleToTarget;
    Vector2 target;
    Vector2 tmp;

    Eater(GameScreen gs, String textureName){
        super(gs, textureName);
        tmp = new Vector2();
        target = new Vector2();
        scale = Rules.SCALE_EATER;
        satiety = 0.1f;
    }

    public void update(float dt){
        super.update(dt);
        velocity.scl(0.98f);
        position.mulAdd(velocity, dt);
    }

    void getSpeed(float dt){
        if (angle < angleToTarget) angle = (Math.abs(angle - angleToTarget) <= 180.0f)? angle + 180.0f *dt : angle - 180.0f *dt;
        if (angle > angleToTarget) angle = (Math.abs(angle - angleToTarget) <= 180.0f)? angle - 180.0f *dt : angle + 180.0f *dt;
        if (angle < 0.0f) angle += 360.0f;
        if (angle > 360.0f) angle -= 360.0f;
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
    }

    public void init(){
        super.init();
        scale = Rules.SCALE_EATER;
    }

    public void smite(Eater another){
        if (this.scale * this.halfWidth < another.scale * another.halfWidth) another.gorge(this);
        else this.gorge(another);

    }

    public void gorge(GamePoint another){
        this.scale += another.satiety;
        if (this.scale <= Rules.MIN_SCALE) this.scale = Rules.MIN_SCALE;
        another.active = false;
    }

    public boolean isRunOver(GamePoint another) {
        float distance = getDistance(another);
        return  distance < (this.scale * this.halfWidth) || distance < (another.scale * another.halfWidth);
    }

    float getDistance(GamePoint another){
        return this.position.dst(another.position);
    }
}
