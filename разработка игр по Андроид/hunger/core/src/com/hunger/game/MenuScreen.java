package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MenuScreen implements Screen {

    private final StringBuilder stringBuilder = new StringBuilder(Rules.HIGH_SCORE);
    private SpriteBatch batch;
    private TextureRegion regionPicture;
    private BitmapFont font92;
    private BitmapFont font48;
    private Skin skin;
    private Stage stageDialog;
    private Dialog dialog;
    private Stage stage;

    MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        regionPicture = Assets.getInstance().getAtlas().findRegion("headpiece");

        font92 = Assets.getInstance().getAssetManager().get("gomarice92.ttf");
        font48 = Assets.getInstance().getAssetManager().get("gomarice48.ttf");

        stage = new Stage(ScreenManager.getInstance().getViewPort(), batch);
        Gdx.input.setInputProcessor(stage);

        BitmapFont font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf");
        skin = new Skin(Assets.getInstance().getAtlas());
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
                ScreenManager.getInstance().getGs().setLoadSaveGame(false);
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        buttonLoadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Gdx.files.local(Rules.SAVE_FILE).exists()) {
                    ScreenManager.getInstance().getGs().setLoadSaveGame(true);
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                }else{
                    ScreenManager.getInstance().getGs().setLoadSaveGame(false);
                    Gdx.input.setInputProcessor(stageDialog);
                    dialog.show(stageDialog);
                }
            }
        });

        buttonExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        getHighScore();
        generateErrorDialog();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font92.draw(batch, "H U N G E R", Rules.WORLD_WIDTH / 2 - 200.0f, Rules.WORLD_HEIGHT - font92.getLineHeight() / 2);
        font48.draw(batch, stringBuilder, Rules.WORLD_WIDTH / 2 - 150.0f, Rules.WORLD_HEIGHT - font92.getLineHeight() - font48.getLineHeight());
        batch.draw(regionPicture,Rules.WORLD_WIDTH / 2 - regionPicture.getRegionWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2);
        batch.end();
        stage.draw();
        stageDialog.draw();
    }

    private void getHighScore(){
        stringBuilder.delete(Rules.HIGH_SCORE.length(), stringBuilder.length);
        if (Gdx.files.local(Rules.PATH_SCORE_HUNGER).exists()){
            try (DataInput input = new DataInput(Gdx.files.local(Rules.PATH_SCORE_HUNGER).read())){
                 stringBuilder.append(input.readInt());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else stringBuilder.append(0);
    }

    private void generateErrorDialog(){
        BitmapFont font26 = Assets.getInstance().getAssetManager().get("gomarice26.ttf");
        skin.add("font26", font26);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font26, Color.RED, skin.getDrawable("windowDialog"));

        Button butOK = new Button(skin.getDrawable("OK"));

        Label label = new Label(String.format(" File %s not found or corrupted! ", Rules.SAVE_FILE), skin, "font32", Color.RED);

        stageDialog = new Stage(ScreenManager.getInstance().getViewPort(), batch);

        dialog = new Dialog(" file upload error", windowStyle){
            @Override
            public void result(Object object){
                if ((int)object == 0){
                    Gdx.input.setInputProcessor(stage);
                    dialog.hide();
                }
            }
        };
        dialog.text(label);
        dialog.button(butOK, 0);
        dialog.padBottom(5.0f);
    }

    private void update(float dt){
        stage.act(dt);
        stageDialog.act(dt);
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
        stageDialog.dispose();
        stage.dispose();
        Assets.getInstance().clear();
    }
}
