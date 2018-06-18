package com.hunger.game.units;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.Assets;
import com.hunger.game.GameScreen;
import com.hunger.game.Poolable;
import com.hunger.game.Rules;

public abstract class GamePoint implements Poolable {

    GameScreen gs;
    TextureRegion region;
    Vector2 position;
    Vector2 velocity;
    float scale;
    float satiety;
    float angle;
    int width;
    int height;
    int halfWidth;
    int halfHeight;
    boolean active;

    public Vector2 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    GamePoint(GameScreen gs, String textureName){
        this.gs = gs;
        if (textureName != null){
            this.region = Assets.getInstance().getAtlas().findRegion(textureName);
            toSize();
        }
        position = new Vector2();
        velocity = new Vector2();
        scale = 1.0f;
    }

    void toSize(){
        width = region.getRegionWidth();
        height = region.getRegionHeight();
        halfWidth = region.getRegionWidth() / 2;
        halfHeight = region.getRegionHeight() / 2;
    }

    public void render(SpriteBatch batch){
        batch.draw(region, position.x - halfWidth, position.y - halfHeight, halfWidth, halfHeight, width, height, scale, scale, angle);
    }

    public void init(){
        position.set(getCoordinate(Rules.GLOBAL_WIDTH), getCoordinate(Rules.GLOBAL_HEIGHT));
        active = true;
    }

    public void update(float dt){
        if (position.x < -halfWidth) position.x = Rules.GLOBAL_WIDTH + halfWidth;
        if (position.y < -halfHeight) position.y = Rules.GLOBAL_HEIGHT + halfHeight;
        if (position.x > Rules.GLOBAL_WIDTH + halfWidth) position.x = -halfWidth;
        if (position.y > Rules.GLOBAL_HEIGHT + halfHeight) position.y = -halfHeight;
    }

    private int getCoordinate(int limit){
        return MathUtils.random(limit);
    }
}
