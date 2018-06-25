package com.hunger.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

import java.io.Serializable;

import static com.hunger.game.units.Food.Type.LEMON;

public class Enemy extends Eater implements Serializable{
    private Eater hero;
    private float distance;

    public Enemy(GameScreen gs){
        super(gs, "bouaaaaah");
        hero = gs.getHero();
        acceleration = getRandomAcceleration();
        active = false;
    }

    public void init(){
        super.init();
        scale = scale + MathUtils.random(-0.02f, 0.02f);
        satiety = scale;
    }
    @Override
    public void update(float dt){
        super.update(dt);
        targetSelection(dt);
        for (int i = 1; i < gs.getHooligans().getActiveList().size(); i++) {
            if(gs.getHooligans().getActiveList().get(i) != this && gs.getHooligans().getActiveList().get(i).isRunOver(this)){
                float rebound = gs.getHooligans().getActiveList().get(i).width * gs.getHooligans().getActiveList().get(i).scale;
                this.velocity.add(MathUtils.random(-rebound, rebound), MathUtils.random(-rebound, rebound));
            }
        }
        getSpeed(dt);
    }

    private void targetSelection(float dt){
        float near = Rules.GLOBAL_WIDTH;
        if (hero.isActive() && hero.scale < this.scale){
            tmp.set(hero.position.mulAdd(hero.velocity, dt));
            angleToTarget = tmp.sub(this.position).angle();
        }else{
            for (int i = 0; i < gs.getFoods().getActiveList().size(); i++){
                if (isDiscovered(gs.getFoods().getActiveList().get(i), dt)){
                    if (gs.getFoods().getActiveList().get(i).getType() == LEMON) angleToTarget = turnAround();
                    return;
                }else if (gs.getFoods().getActiveList().get(i).getType() != LEMON){
                    if (near > distance){
                        near = distance;
                        tmp.set(gs.getFoods().getActiveList().get(i).position);
                    }
                }
            }
            angleToTarget = tmp.sub(this.position).angle();
        }
        if (isDiscovered(hero, dt)) {
            if (hero.scale > this.scale) angleToTarget = turnAround();
        }
        stumbleOn(dt);
    }

    private void stumbleOn(float dt){
        for (int i = 0; i < gs.getWaste().getActiveList().size(); i++) {
            if (isDiscovered(gs.getWaste().getActiveList().get(i), dt)){
                angleToTarget = turnAround();
                return;
            }
        }
    }

    private float turnAround(){
        return (angleToTarget < 180.0f) ? angleToTarget + 90.0f : angleToTarget - 90.0f;
    }

    private boolean isDiscovered(GamePoint unit, float dt){
        if (unit.isActive()){
            float radiusOfDetection = (scale >= Rules.SCALE_EATER) ? width * scale : width * Rules.SCALE_EATER;
            distance = getDistance(unit);
            float ratio = (unit == hero && hero.scale / this.scale > 1.0f) ? hero.scale / this.scale : 1.0f;
            if (distance <= radiusOfDetection + unit.halfWidth * unit.scale * ratio){
                target.set(unit.position.mulAdd(unit.velocity, dt));
                tmp.set(target);
                angleToTarget = tmp.sub(this.position).angle();
                acceleration += acceleration * ratio * dt;
                return true;
            }
        }
        acceleration = getRandomAcceleration();
        return false;
    }
}
