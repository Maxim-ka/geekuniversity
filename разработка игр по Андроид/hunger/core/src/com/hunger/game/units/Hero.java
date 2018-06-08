package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class Hero extends Eater{

    public Hero(GameScreen gs) {
        super(gs, "hero");
        position.set(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2);
        acceleration = 300.0f;
        angle = -90.0f;
        active = true;
    }

    public void init(){
        super.init();
        angle = -90.0f;
    }

    public void update(float dt){
        if (isActive()) super.update(dt);
        else init();
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            target.set(Gdx.input.getX(), Rules.WORLD_HEIGHT - Gdx.input.getY());
            tmp.set(target);
            angleToTarget = tmp.sub(position).angle();
            getSpeed(dt);
        }
    }
}
