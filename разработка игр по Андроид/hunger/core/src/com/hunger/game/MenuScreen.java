package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

    private SpriteBatch batch;
    private TextureRegion regionPicture;
    private BitmapFont font32;
    private BitmapFont font92;
    private Stage stage;

    MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        regionPicture = Assets.getInstance().getAtlas().findRegion("headpiece");
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf");
        font92 = Assets.getInstance().getAssetManager().get("gomarice92.ttf");
        stage = new Stage(ScreenManager.getInstance().getViewPort(), batch);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Assets.getInstance().getAtlas());
        skin.add("font32", font32);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.font = skin.getFont("font32");
        skin.add("buttonStyle", buttonStyle);

        Button buttonNewGame = new TextButton("Start New Game", skin, "buttonStyle");
        Button buttonLoadGame = new TextButton("Load Game", skin, "buttonStyle");
        Button buttonExitGame = new TextButton("Exit", skin, "buttonStyle");
        buttonNewGame.setPosition(buttonStyle.up.getMinWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2 - buttonStyle.up.getMinHeight());
        buttonLoadGame.setPosition(Rules.WORLD_WIDTH / 2 - buttonStyle.up.getMinWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2 - buttonStyle.up.getMinHeight());
        buttonExitGame.setPosition(Rules.WORLD_WIDTH - buttonStyle.up.getMinWidth() - buttonStyle.up.getMinWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2 - buttonStyle.up.getMinHeight());
        stage.addActor(buttonNewGame);
        stage.addActor(buttonLoadGame);
        stage.addActor(buttonExitGame);

        buttonNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        buttonExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font92.draw(batch, "H U N G E R", Rules.WORLD_WIDTH / 2 - 200.0f, Rules.WORLD_HEIGHT - font92.getLineHeight() / 2);
        batch.draw(regionPicture,Rules.WORLD_WIDTH / 2 - regionPicture.getRegionWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2);
        batch.end();
        stage.draw();
    }

    private void update(float dt){
        stage.act(dt);
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
        Assets.getInstance().getAtlas().dispose();
        stage.dispose();
    }
}
