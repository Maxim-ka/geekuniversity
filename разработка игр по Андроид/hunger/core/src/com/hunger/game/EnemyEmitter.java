package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hunger.game.units.Enemy;

public class EnemyEmitter extends ObjectPool<Enemy>{

    private static final int NUMBER_ENEMY = 3;
    private static final float CREATION_TIME_ENEMY = 5.0f;
    private transient GameScreen gs;
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
        if (activeList.size() <= number && activeList.size() < NUMBER_ENEMY + gs.getLevel() * NUMBER_ENEMY) {
            float interval = CREATION_TIME_ENEMY - gs.getLevel() / CREATION_TIME_ENEMY;
            time += dt;
            if (time >= interval){
                time = 0;
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

    void setLoadedEnemyEmitter(GameScreen gs){
        this.gs = gs;
        for (int i = 0; i < freeList.size(); i++) {
            freeList.get(i).reload(gs);
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).reload(gs);
        }
    }
}
