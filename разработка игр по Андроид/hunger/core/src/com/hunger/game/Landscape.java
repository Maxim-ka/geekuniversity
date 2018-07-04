package com.hunger.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.units.Hero;

import java.io.Serializable;

public class Landscape implements Serializable {

    private transient TextureRegion flooring;
    private transient TextureRegion tree;
    private final int cellSize;
    private final int numberCellX, numberCellY;
    private final byte[][] data;
    private transient GameScreen gs;

    Landscape(GameScreen gs) {
        this.gs = gs;
        flooring = Assets.getInstance().getAtlas().findRegion("embossedTile");
        tree = Assets.getInstance().getAtlas().findRegion("treeInBox");
        cellSize = flooring.getRegionWidth();
        numberCellX = Rules.GLOBAL_WIDTH / cellSize;
        numberCellY = Rules.GLOBAL_HEIGHT / cellSize;
        data = new byte[numberCellX][numberCellY];
    }

    public void initMapLevel(Hero hero){
        for (int i = 0; i < numberCellX; i++) {
            for (int j = 0; j < numberCellY; j++) {
                if (MathUtils.random(100) < 3 + gs.getLevel() && !isPlaceForHero(hero, i, j)) data[i][j] = 1;
                else data[i][j] = 0;
            }
        }
    }

    public void render(SpriteBatch batch){
        for (int i = 0; i < numberCellX; i++) {
            for (int j = 0; j < numberCellY; j++) {
                batch.draw(flooring, i * cellSize, j * cellSize);
                if (data[i][j] == 1) batch.draw(tree, i * cellSize , j * cellSize);
            }
        }
    }

    public void update(float dt){

    }

    private boolean isPlaceForHero(Hero hero, int cellX, int cellY) {
        if((int)hero.getPosition().x /cellSize == cellX && (int)hero.getPosition().y / cellSize == cellY) return true;
        int numberPoint = 8;
        float radius = hero.getScale() * hero.getHalfWidth();
        for (int i = 0; i < numberPoint; i++) {
            float tmpX = hero.getPosition().x + radius * (float) Math.cos(2 * Math.PI / numberPoint * i);
            float tmpY = hero.getPosition().y + radius * (float) Math.sin(2 * Math.PI / numberPoint * i);
            if (cellX == (int) tmpX / cellSize && cellY == (int) tmpY / cellSize) return true;
        }
        return false;
    }

    private boolean isCellEmpty(float x, float y){
        int cellX = (int) x/ cellSize;
        int cellY = (int) y/ cellSize;
        if (cellX >= numberCellX) cellX = 0;
        if (cellX < 0) cellX = numberCellX - 1;
        if (cellY >= numberCellY) cellY = 0;
        if (cellY < 0) cellY = numberCellY - 1;
        return data[cellX][cellY] != 1;
    }

    public boolean isCellEmpty(float x, float y, float radius){
        if (!isCellEmpty(x, y)) return false;
        int numberPoint = 8;
        for (int i = 0; i < numberPoint; i++) {
            float tmpX = x + radius * (float) Math.cos(2 * Math.PI / numberPoint * i);
            float tmpY = y + radius * (float) Math.sin(2 * Math.PI / numberPoint * i);
            int cellX = (int) tmpX/ cellSize;
            int cellY = (int) tmpY/ cellSize;
            if (cellX >= numberCellX) cellX = 0;
            if (cellX < 0) cellX = numberCellX - 1;
            if (cellY >= numberCellY) cellY = 0;
            if (cellY < 0) cellY = numberCellY - 1;
            if (data[cellX][cellY] == 1) return false;
        }
        return true;
    }

    public void setLoadedLandscape(GameScreen gs){
        flooring = Assets.getInstance().getAtlas().findRegion("embossedTile");
        tree = Assets.getInstance().getAtlas().findRegion("treeInBox");
        this.gs = gs;
    }
}
