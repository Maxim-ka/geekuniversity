package com.hunger.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.hunger.game.Rules;

public abstract class GamePoint {

    Texture texture;
    Vector2 position;
    Vector2 velocity;
    float scale;
    float satiety;
    float angle;
    int width;
    private int height;
    int halfWidth;
    private int halfHeight;

    GamePoint(Texture texture){
        this.texture = texture;
        width = texture.getWidth();
        height = texture.getHeight();
        halfWidth = texture.getWidth() / 2;
        halfHeight = texture.getHeight() / 2;
        position = new Vector2(getCoordinate(Rules.WORLD_WIDTH), getCoordinate(Rules.WORLD_HEIGHT));
        scale = 1.0f;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - halfWidth, position.y - halfHeight, halfWidth, halfHeight, width, height, scale, scale, angle, 0,0, width,height, false, false );
    }

    public void update(float dt){
        if (position.x < -32) {
            position.x = 1312;
        }
        if (position.y < -32) {
            position.y = 752;
        }
        if (position.x > 1312) {
            position.x = -32;
        }
        if (position.y > 752) {
            position.y = -32;
        }
    }

    public boolean isRunOver(GamePoint another) {
        float distance = this.position.dst(another.position);
        return  distance < (this.scale * this.halfWidth) || distance < (another.scale * another.halfWidth);
    }

    int getCoordinate(int limit){
        return MathUtils.random(limit);
    }

    public void dispose(){
        texture.dispose();
    }
}
