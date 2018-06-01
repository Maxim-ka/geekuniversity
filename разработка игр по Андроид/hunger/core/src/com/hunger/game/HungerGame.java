package com.hunger.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.hunger.game.units.*;

public class HungerGame extends ApplicationAdapter {
    private static final int NUMBER_FOODS = 10;
    private static final int NUMBER_EATERS = 5;
    private final Eater[] eaters = new Eater[NUMBER_EATERS];
    private final Food[] foods = new Food[NUMBER_FOODS];
    private SpriteBatch batch;
	private Texture textureEnemy;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
        for (int i = 0; i < NUMBER_FOODS; i++) {
            foods[i] = new Food(Food.Type.values()[MathUtils.random(Food.Type.values().length - 1)]);
        }
		textureEnemy = new Texture("bouaaaaah_256.png");
        eaters[0] = new Hero();
		for (int i = 1; i <eaters.length ; i++) {
            eaters[i] = new Enemy(textureEnemy, eaters, foods);
        }
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        for(int j = 0; j < NUMBER_FOODS; j++) {
            foods[j].render(batch);
        }
        for (int i = 0; i < NUMBER_EATERS; i++){
            eaters[i].render(batch);
        }
		batch.end();
	}

	private void update(float dt){
        for (int i = 0; i < NUMBER_EATERS ; i++) {
            eaters[i].update(dt);
        }
        for (int i = 0; i < NUMBER_FOODS; i++) {
            foods[i].update(dt);
        }
		checkForEating();
	}

    private void checkForEating(){
        for (int i = 0; i < NUMBER_EATERS; i++) {
            if (eaters[i] != eaters[0]) eaters[i].gorge(eaters[0]);
            for (int j = 0; j < NUMBER_FOODS; j++) {
                if (eaters[i].isRunOver(foods[j])){
                    eaters[i].gorge(foods[j]);
                    foods[j].changeTypeFood();
                }
            }
        }
    }

	@Override
	public void dispose () {
		batch.dispose();
		textureEnemy.dispose();
		eaters[0].dispose();
        foods[0].dispose();
	}
}
