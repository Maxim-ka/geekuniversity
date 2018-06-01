package com.hunger.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.Rules;

public abstract class Eater extends GamePoint {

    float acceleration;
    float angleToTarget;
    Vector2 target;
    Vector2 tmp;

    Eater(Texture texture){
        super(texture);
        velocity = new Vector2(0.0f, 0.0f);
        tmp = new Vector2(0.0f, 0.0f);
        target = new Vector2(0.0f, 0.0f);
        scale = Rules.SCALE_EATER;
        satiety = 0.1f;
    }

    public void update(float dt){
        super.update(dt);
        if (angle < angleToTarget) angle = (Math.abs(angle - angleToTarget) <= 180.0f)? angle + 180.0f *dt : angle - 180.0f *dt;
        if (angle > angleToTarget) angle = (Math.abs(angle - angleToTarget) <= 180.0f)? angle - 180.0f *dt : angle + 180.0f *dt;
        if (angle < 0.0f) angle += 360.0f;
        if (angle > 360.0f) angle -= 360.0f;
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        velocity.scl(0.96f);
        position.mulAdd(velocity, dt);
    }

    public void gorge(GamePoint another){
        if (isRunOver(another)){
            if (this.getClass().getSuperclass().getSimpleName().equals(another.getClass().getSuperclass().getSimpleName())){
                if (this.scale * this.halfWidth < another.scale * another.halfWidth){
                    another.scale += this.satiety;
                    this.scale = 0.25f;
                    this.position.set(getCoordinate(Rules.WORLD_WIDTH), getCoordinate(Rules.WORLD_HEIGHT));
                    return;
                }else another.scale = 0.25f;
            }
            this.scale += another.satiety;
            if (this.scale <= Rules.MIN_SCALE) this.scale = Rules.MIN_SCALE;
            another.position.set(getCoordinate(Rules.WORLD_WIDTH), getCoordinate(Rules.WORLD_HEIGHT));
        }
    }

}
