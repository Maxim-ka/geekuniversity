package com.hunger.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;

public class HungerGame extends ApplicationAdapter {
    private static final int NUMBER_FOODS = 10;
	SpriteBatch batch;
	Hero hero;
	Food[] foods;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		hero = new Hero();
		foods = scatterFood();
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        for (Food food : foods) {
            food.render(batch);
        }
		hero.render(batch);
		batch.end();
	}

	private void update(float dt){
		hero.update(dt);
        for (Food food : foods) {
            food.update(dt);
        }
	}

	private Food[] scatterFood(){
        Food[] foods = new Food[NUMBER_FOODS];
        for (int i = 0; i < NUMBER_FOODS; i++) {
            foods[i] = (new Food(hero));
        }
        return foods;
    }

	@Override
	public void dispose () {
		batch.dispose();
		hero.dispose();
        for (Food food : foods) {
            food.dispose();
        }
	}
}
