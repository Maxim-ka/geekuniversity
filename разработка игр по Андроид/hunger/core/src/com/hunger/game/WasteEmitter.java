package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hunger.game.units.Waste;

public class WasteEmitter extends ObjectPool<Waste> {

    private GameScreen gs;
    private int numEnemy;
    private int numFood;

    WasteEmitter(GameScreen gs){
        this.gs = gs;
        numEnemy = gs.getEnemies().freeList.size() * 2;
        numFood = gs.getEnemies().freeList.size() * gs.getFoods().freeList.size();
        addObjectsToFreeList(numEnemy + numFood);
    }

    void render(SpriteBatch batch){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    Waste getThorn(){
        for (int i = freeList.size() - 1; i >= 0; i--) {
            if (freeList.get(i).getType() == Waste.Type.THORN){
                activeList.add(freeList.remove(i));                ;
                return activeList.get(activeList.size() - 1);
            }
        }
        activeList.add(new Waste(gs, Waste.Type.THORN));
        return activeList.get(activeList.size() - 1);
    }

    Waste getCorpse(){
        for (int i = freeList.size() - 1; i >= 0; i--) {
            if (freeList.get(i).getType() == Waste.Type.CORPSE){
                activeList.add(freeList.remove(i));
                return activeList.get(activeList.size() - 1);
            }
        }
        activeList.add(new Waste(gs, Waste.Type.CORPSE));
        return activeList.get(activeList.size() - 1);
    }

    void update(float dt){
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    @Override
    protected Waste newObject() {
        if (numFood > 0){
            numFood--;
            return new Waste(gs, Waste.Type.THORN);
        }
        return new Waste(gs, Waste.Type.CORPSE);
    }
}
