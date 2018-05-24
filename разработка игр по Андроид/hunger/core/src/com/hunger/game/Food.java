package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Food {

    private final Texture texture = new Texture("pizza.png");
    private final int halfWidth = texture.getWidth() / 2;
    private final int halfHeight = texture.getHeight() / 2;
    private final float satiety = 0.05f;
    private Vector2 location;
    private Hero hero;

    Food(Hero hero){
        this.hero = hero;
        location = new Vector2(getCoordinate(Gdx.graphics.getWidth()), getCoordinate(Gdx.graphics.getHeight()));
    }

    void update(float dt){
        float distance = location.dst(hero.getPosition());
        if (distance < hero.getSwallowWidth() || distance < hero.getSwallowHeight()){
            hero.setScale(hero.getScale() + satiety);
            hero.setSwallowWidth(hero.getHalfWidth() * hero.getScale());
            hero.setSwallowHeight(hero.getHalfHeight() * hero.getScale());
            location.set(getCoordinate(Gdx.graphics.getWidth()), getCoordinate(Gdx.graphics.getHeight()));
        }
    }

    void render(SpriteBatch batch){
        batch.draw(texture, location.x - halfWidth, location.y - halfHeight);
    }

    private int getCoordinate(int limit){
        return MathUtils.random(limit);
    }

    void dispose(){
        texture.dispose();
    }
}
