package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.Rules;

public class Hero extends Eater{

    public Hero() {
        super(new Texture("hero_smile_256.png"));
        position = new Vector2(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2);
    }

    public void update(float dt){
        super.update(dt);
        if (Gdx.input.isTouched()){
            target.set(Gdx.input.getX(), Rules.WORLD_HEIGHT - Gdx.input.getY());
            tmp.set(target);
            angleToTarget = tmp.sub(position).angle();
            acceleration = 300.0f;
        }else{
            acceleration = 0.0f;
            angle = -90.0f;
        }
    }
}
