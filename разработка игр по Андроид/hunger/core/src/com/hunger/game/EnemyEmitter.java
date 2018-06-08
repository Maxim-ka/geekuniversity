package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hunger.game.units.Enemy;

public class EnemyEmitter extends ObjectPool<Enemy> {

    private GameScreen gs;
    private int number;
    private float time;

    EnemyEmitter(GameScreen gs, int number){
        this.gs = gs;
        this.number = number;
        addObjectsToFreeList(number);
    }

    void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    void update(float dt){
        if (activeList.size() < number) {
            time += dt;
            if (time >= 2.0f){
                time = 0.0f;
                getActiveElement().init();
            }
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(gs);
    }
}
