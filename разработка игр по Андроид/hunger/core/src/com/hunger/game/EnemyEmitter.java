package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hunger.game.units.Enemy;

import java.io.Serializable;

public class EnemyEmitter extends ObjectPool<Enemy>{

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
        if (activeList.size() < number + gs.getLevel()) {
            float interval = 5.0f - gs.getLevel() / 5.0f;
            time += dt;
            if (time >= interval){
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

    public void setLoadedEnemyEmitter(GameScreen gs){
        this.gs = gs;
        for (int i = 0; i < freeList.size(); i++) {
            freeList.get(i).reload(gs);
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).reload(gs);
        }
    }
}
