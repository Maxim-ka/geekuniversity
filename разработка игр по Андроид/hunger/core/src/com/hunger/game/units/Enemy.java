package com.hunger.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.Rules;

import static com.hunger.game.units.Food.Type.LEMON;

public class Enemy extends Eater{
    private float minRadiusOfDetection = halfWidth * 0.5f;
    private Eater hero;
    private Eater[] eaters;
    private Food[] foods;

    public Enemy(Texture texture, Eater[] eaters, Food[] foods){
        super(texture);
        this.eaters = eaters;
        this.foods = foods;
        hero = eaters[0];
    }

    public void update(float dt){
        super.update(dt);
        for (int i = 0; i < eaters.length; i++) {
            if (eaters[i] == hero){
                if (isDiscovered(hero, dt)) {
                    if (hero.scale > this.scale) angleToTarget += 180.0f;
                    rush(dt);
                }
            }else if(eaters[i] != this && eaters[i].isRunOver(this)){
                float rebound = eaters[i].width * eaters[i].scale;
                this.velocity.add(MathUtils.random(-rebound, rebound), MathUtils.random(-rebound, rebound));
            }
        }

        for (int i = 0; i < foods.length; i++) {
            if (isDiscovered(foods[i], dt)){
                if (foods[i].getType() == LEMON) angleToTarget = (angleToTarget < 180.0f) ? angleToTarget + 180.0f : angleToTarget - 180.0f;
                rush(dt);
            }
        }
    }

    private void rush(float dt){
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angleToTarget)) * dt, acceleration * (float) Math.sin(Math.toRadians(angleToTarget)) * dt);
        position.mulAdd(velocity, dt);
    }

    private boolean isDiscovered(GamePoint unit, float dt){
        float radiusOfDetection = (scale >= Rules.SCALE_EATER) ? width * scale : minRadiusOfDetection;
        if (this.position.dst(unit.position) <= radiusOfDetection + unit.halfWidth * unit.scale){
            target.set(unit.position.mulAdd(unit.velocity, dt));
            tmp.set(target);
            angleToTarget = tmp.sub(this.position).angle();
            acceleration += acceleration;
            return true;
        }
        acceleration = getRandomAcceleration();
        return false;
    }

    private float getRandomAcceleration(){
        return MathUtils.random(100.0f, 300.0f);
    }
}
