package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.DataInput;

import java.io.IOException;

public class GameOverScreen implements Screen {

    private static final String YOUR = "your ";
    private final StringBuilder stringBuilder = new StringBuilder(Rules.HIGH_SCORE);
    private BitmapFont font72;
    private SpriteBatch batch;
    private TextureRegion regionPicture;
    private Stage stage;

    GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        regionPicture = Assets.getInstance().getAtlas().findRegion("overGame");

        stage = new Stage(ScreenManager.getInstance().getViewPort(), batch);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Assets.getInstance().getAtlas());

        font72 = Assets.getInstance().getAssetManager().get("gomarice72.ttf");
        BitmapFont font48 = Assets.getInstance().getAssetManager().get("gomarice48.ttf");
        skin.add("font48", font48);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("menu");
        buttonStyle.font = skin.getFont("font48");
        skin.add("buttonStyle", buttonStyle);

        Button buttonMenu = new TextButton("Menu", skin, "buttonStyle");
        buttonMenu.setPosition(Rules.INDENT + buttonStyle.up.getMinWidth() / 2, Rules.WORLD_HEIGHT  - Rules.INDENT - buttonStyle.up.getMinHeight());

        stage.addActor(buttonMenu);

        buttonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        getScore();
    }

    private void getScore(){
        stringBuilder.delete(Rules.HIGH_SCORE.length(), stringBuilder.length());
        int score = ScreenManager.getInstance().getGs().getScore();
        int level = ScreenManager.getInstance().getGs().getLevel();
        try (DataInput input = new DataInput(Gdx.files.local(Rules.PATH_SCORE_HUNGER).read())){
            int highScore = input.readInt();
            if (highScore == score){
                stringBuilder.append(YOUR).append(score).append(" ").append(Rules.LEVEL).append(level);
            }else{
                stringBuilder.append(highScore).append(Rules.LB).append(YOUR).append(Rules.SCORE).append(score)
                        .append(" ").append(Rules.LEVEL).append(level);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(regionPicture,Rules.WORLD_WIDTH / 2 - regionPicture.getRegionWidth() / 2, Rules.WORLD_HEIGHT / 2 - regionPicture.getRegionHeight() / 2);
        font72.draw(batch, stringBuilder, Rules.WORLD_WIDTH / 2 - 300, Rules.WORLD_HEIGHT / 2 + font72.getLineHeight() / 2);
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
        stage.dispose();
        Assets.getInstance().clear();
    }
}
