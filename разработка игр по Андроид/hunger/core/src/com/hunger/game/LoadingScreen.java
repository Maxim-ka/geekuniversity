package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingScreen implements Screen {

    private SpriteBatch batch;
    private Texture bar;
    private Texture picture;

    LoadingScreen(SpriteBatch batch){
        this.batch = batch;
    }

    @Override
    public void show() {
        picture = new Texture("headpiece.png");
        Pixmap pixmap = new Pixmap(picture.getWidth(), 20, Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        bar = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Assets.getInstance().getAssetManager().update()){
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().gotoTarget();
        }
        batch.begin();
        batch.draw(picture,Rules.WORLD_WIDTH / 2 - picture.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - picture.getHeight() / 2);
        batch.draw(bar, Rules.WORLD_WIDTH / 2 - bar.getWidth() / 2, Rules.WORLD_HEIGHT / 2 - picture.getHeight() / 2 - 2 * bar.getHeight(),picture.getWidth() * Assets.getInstance().getAssetManager().getProgress(), bar.getHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        picture.dispose();
        bar.dispose();
    }
}
