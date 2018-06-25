package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.units.Food;

import java.io.Serializable;

public class FoodEmitter extends ObjectPool<Food> implements Serializable {

    private final String[] textureName = {"pizza", "lemon", "doughnut"};
    private final TextureRegion[] regions = new TextureRegion[textureName.length];
    private transient GameScreen gs;
    private int number;
    private float time;

    public TextureRegion[] getRegions() {
        return regions;
    }

    public void setGs(GameScreen gs) {
        this.gs = gs;
    }

    FoodEmitter(GameScreen gs, int number){
        this.gs = gs;
        this.number = number;
        toRegions();
        addObjectsToFreeList(number);
    }

    public void toRegions(){
        for (int i = 0; i < regions.length; i++) {
            regions[i] = Assets.getInstance().getAtlas().findRegion(textureName[i]);
        }
    }

    void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    private void generateFood() {
        int shareOfCalories = 50 + gs.getLevel()* 5;
        int shareBadFood = 10 + gs.getLevel() * 5;
        Food.Type goodFood = (MathUtils.random(100) < shareOfCalories) ? Food.Type.PIZZA : Food.Type.DOUGHNUT;
        Food.Type type = (MathUtils.random(100) < shareBadFood) ? Food.Type.LEMON : goodFood;
        getActiveElement().init(type);
    }

    void update(float dt){
        if (activeList.size() < number) {
            time += dt;
            if (time >= 0.5f + 0.5f * gs.getLevel()){
                time = 0.0f;
                generateFood();
            }
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    @Override
    protected Food newObject() {
        return new Food(gs, regions);
    }
}
