package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.units.Food;

public class FoodEmitter extends ObjectPool<Food> {

    private GameScreen gs;
    private int number;
    private float time;

    FoodEmitter(GameScreen gs, int number){
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
            if (time >= 0.5f){
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
    protected Food newObject() {
        return new Food(gs, Food.Type.values()[MathUtils.random(Food.Type.values().length - 1)]);
    }
}
