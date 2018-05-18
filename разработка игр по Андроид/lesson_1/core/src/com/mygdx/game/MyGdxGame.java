package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private Asteroid asteroid;
	private Aim aim;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		asteroid = new Asteroid();
		aim = new Aim();
	}

	@Override
	public void render () {
		float dt = 	Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		aim.render(batch);
		asteroid.render(batch);
		batch.end();
	}

	private void update(float dt){
		asteroid.update(dt);
		aim.update(dt);
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			if (!asteroid.isActive()) asteroid.activated(aim.getAngle(), aim.getDistance());
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		asteroid.dispose();
		aim.dispose();
	}
}
