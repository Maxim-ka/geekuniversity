package com.hunger.game.units;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.GameScreen;
import com.hunger.game.Poolable;
import com.hunger.game.Rules;

public abstract class GamePoint implements Poolable {

    GameScreen gs;
    TextureRegion texture;
    Vector2 position;
    Vector2 velocity;
    float scale;
    float satiety;
    float angle;
    int width;
    private int height;
    int halfWidth;
    private int halfHeight;
    boolean active;

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    GamePoint(GameScreen gs, String textureName){
        this.gs = gs;
        this.texture = gs.getAtlas().findRegion(textureName);
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
        halfWidth = texture.getRegionWidth() / 2;
        halfHeight = texture.getRegionHeight() / 2;
        position = new Vector2();
        velocity = new Vector2();
        scale = 1.0f;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - halfWidth, position.y - halfHeight, halfWidth, halfHeight, width, height, scale, scale, angle);
    }

    public void init(){
        position.set(getCoordinate(Rules.WORLD_WIDTH), getCoordinate(Rules.WORLD_HEIGHT));
        active = true;
    }

    public void update(float dt){
        if (position.x < -halfWidth) position.x = Rules.WORLD_WIDTH + halfWidth;
        if (position.y < -halfHeight) position.y = Rules.WORLD_HEIGHT + halfHeight;
        if (position.x > Rules.WORLD_WIDTH + halfWidth) position.x = -halfWidth;
        if (position.y > Rules.WORLD_HEIGHT + halfHeight) position.y = -halfHeight;
    }

    private int getCoordinate(int limit){
        return MathUtils.random(limit);
    }
}
