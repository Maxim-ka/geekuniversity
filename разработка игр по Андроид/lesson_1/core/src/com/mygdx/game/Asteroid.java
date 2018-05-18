package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Asteroid {
    private final float impulse = 250.0f;
    private final float mass = 100.0f;
    private Texture texture = new Texture("asteroid64.png");
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private boolean active;
    private int width = texture.getWidth();
    private int height = texture.getHeight();
    private int halfWidth = width / 2;
    private int halfHeight = height / 2;
    private float startGravity;

    public boolean isActive() {
        return active;
    }

    void activated(float angle, float distance){
        x = 0;
        y = 0;
        vx = (float) Math.cos(angle) * impulse;
        vy = (float) Math.sin(angle) * impulse;
        startGravity = distance * (float)Math.cos(angle);
        active = true;
    }

    void render(SpriteBatch batch){
        if (active){
            batch.draw(texture, x - halfWidth, y - halfHeight, halfWidth, halfHeight, width, height, 1,1,angle,0,0,width, height,false,false);
        }
    }

    void update(float dt){
        angle -= 180.0f * dt;
        x += vx * dt;
        if (x > Gdx.app.getGraphics().getWidth()) active = false;
        y += vy * dt;
        if (x >= startGravity) vy = (y <= 0) ? -vy  : vy - mass * dt;
    }

    void dispose(){
        texture.dispose();
    }
}
