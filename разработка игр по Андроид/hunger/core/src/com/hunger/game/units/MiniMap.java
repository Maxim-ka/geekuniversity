package com.hunger.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.Assets;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

import java.io.Serializable;

public class MiniMap extends GamePoint implements Serializable {

    private transient TextureRegion regionMiniHero;
    private transient TextureRegion regionMiniEnemy;
    private transient TextureRegion regionMiniGoodFood;
    private int halfWidthMiniHero;
    private int halfHeightMiniHero;
    private int halfWidthMiniEnemy;
    private int halfHeightMiniEnemy;
    private int halfWidthMiniGoodFood;
    private int halfHeightMiniGoodFood;
    private float scanRadius;
    private Vector2 tmp;

    public void setRegionMiniHero(TextureRegion regionMiniHero) {
        this.regionMiniHero = regionMiniHero;
    }

    public void setRegionMiniEnemy(TextureRegion regionMiniEnemy) {
        this.regionMiniEnemy = regionMiniEnemy;
    }

    public void setRegionMiniGoodFood(TextureRegion regionMiniGoodFood) {
        this.regionMiniGoodFood = regionMiniGoodFood;
    }

    public MiniMap(GameScreen gs) {
        super(gs, "scanerMiniMap");
        regionMiniHero = Assets.getInstance().getAtlas().findRegion("miniHero");
        regionMiniEnemy = Assets.getInstance().getAtlas().findRegion("miniEnemy");
        regionMiniGoodFood = Assets.getInstance().getAtlas().findRegion("miniGoodFood");
        halfWidthMiniHero = regionMiniHero.getRegionWidth() / 2;
        halfHeightMiniHero = regionMiniHero.getRegionHeight()/2;
        halfWidthMiniEnemy = regionMiniEnemy.getRegionWidth() / 2;
        halfHeightMiniEnemy = regionMiniEnemy.getRegionHeight() / 2;
        halfWidthMiniGoodFood = regionMiniGoodFood.getRegionWidth() / 2;
        halfHeightMiniGoodFood = regionMiniGoodFood.getRegionHeight() / 2;
        position.set(Rules.WORLD_WIDTH - halfWidth - Rules.INDENT, halfHeight + Rules.INDENT);
        active = true;
        scanRadius = 1000.0f;
        tmp = new Vector2();
    }

    public void render(SpriteBatch batch){
        super.render(batch);
        if (gs.getHero().isActive()){
            batch.draw(regionMiniHero, this.position.x - halfWidthMiniHero, this.position.y - halfHeightMiniHero);
        }
        for (int i = 0; i < gs.getHooligans().getActiveList().size(); i++) {
            if (isScans(gs.getHooligans().getActiveList().get(i)))
                batch.draw(regionMiniEnemy, this.position.x - halfWidthMiniEnemy + tmp.x, this.position.y + tmp.y - halfHeightMiniEnemy);
        }
        for (int i = 0; i < gs.getFoods().getActiveList().size(); i++) {
            if (gs.getFoods().getActiveList().get(i).getType() != Food.Type.LEMON){
                if (isScans(gs.getFoods().getActiveList().get(i)))
                    batch.draw(regionMiniGoodFood, this.position.x + tmp.x - halfWidthMiniGoodFood, this.position.y + tmp.y - halfHeightMiniGoodFood);
            }
        }
    }

    private boolean isScans(GamePoint unit){
        if (gs.getHero().getDistance(unit) <= scanRadius){
            tmp.set(unit.position);
            tmp.sub(gs.getHero().position);
            tmp.scl(halfWidth / scanRadius);
            return true;
        }
        return false;
    }
}
