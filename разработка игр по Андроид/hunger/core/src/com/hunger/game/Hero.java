package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

class Hero {
    private float angle = -90.0f;;
    private float acceleration = 300.0f;
    private float scale = 1.0f;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private int width;
    private int height;
    private int halfWidth;
    private int halfHeight;
    private float swallowWidth;
    private float swallowHeight;

    private Vector2 positionMouse;
    private Vector2 tmp;

    public int getHalfWidth() {
        return halfWidth;
    }

    public int getHalfHeight() {
        return halfHeight;
    }

    public void setSwallowWidth(float swallowWidth) {
        this.swallowWidth = swallowWidth;
    }

    public void setSwallowHeight(float swallowHeight) {
        this.swallowHeight = swallowHeight;
    }

    public float getSwallowWidth() {
        return swallowWidth;
    }

    public float getSwallowHeight() {
        return swallowHeight;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector2 getPosition() {
        return position;
    }

    Hero() {
        texture = new Texture("hero_smile.png");
        position = new Vector2(640.0f, 360.0f);
        velocity = new Vector2(0.0f, 0.0f);
        width = texture.getWidth();
        height = texture.getHeight();
        halfWidth =  width/ 2;
        halfHeight = height / 2;
        swallowWidth = halfWidth * scale;
        swallowHeight = halfHeight * scale;

        positionMouse  = new Vector2(0.0f, 0.0f);
        tmp = new Vector2(0.0f, 0.0f);
    }

    void update(float dt){
        if (Gdx.input.isTouched()){
            positionMouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            tmp.set(positionMouse);
            float angleToMouse = tmp.sub(position).angle();
            if (angle < angleToMouse) angle = (Math.abs(angle - angleToMouse) <= 180.0f)? angle + 180.0f *dt : angle - 180.0f *dt;
            if (angle > angleToMouse) angle = (Math.abs(angle - angleToMouse) <= 180.0f)? angle - 180.0f *dt : angle + 180.0f *dt;
            if (angle < 0.0f) angle += 360.0f;
            if (angle > 360.0f) angle -= 360.0f;
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
        velocity.scl(0.96f);
        position.mulAdd(velocity, dt);
    }

    void render(SpriteBatch batch){
        batch.draw(texture, position.x - halfWidth, position.y - halfHeight, halfWidth, halfHeight, width, height, scale, scale, angle, 0,0, width,height, false, false );
    }


    void dispose(){
        texture.dispose();
    }
}
