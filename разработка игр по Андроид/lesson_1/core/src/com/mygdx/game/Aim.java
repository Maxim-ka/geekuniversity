package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Aim {
    private final float rotateInDegrees = 90.0f;
    private final float distance = 200.0f;
    private final float maxAngle = (float) (Math.PI / 3);
    private Texture texture = new Texture("aim.png");
    private float angle;
    private float rotate;
    private int width = texture.getWidth();
    private int height = texture.getHeight();
    private int halfWidth = width / 2;
    private int halfHeight = height / 2;

    public float getDistance() {
        return distance;
    }

    public float getAngle() {
        return angle;
    }

    void render(SpriteBatch batch){
        batch.draw(texture, distance * (float)Math.cos(angle) - halfWidth , distance * (float)Math.sin(angle) - halfHeight, halfWidth, halfHeight, width,height,1,1, rotate,0,0,width,height, false,false);
    }

    void update(float dt){
        rotate -= rotateInDegrees * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            angle += maxAngle * dt;
            if (angle > maxAngle) angle = maxAngle;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            angle -= maxAngle * dt;
            if (angle < 0) angle = 0;
        }
    }

    void dispose(){
        texture.dispose();
    }
}
