package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ScreenManager {

    enum ScreenType{
        MENU, GAME;
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private HungerGame game;
    private LoadingScreen ls;
    private GameScreen gs;
    private MenuScreen ms;
    private Screen targetScreen;

    private SpriteBatch batch;
    private FitViewport viewPort;
    private Camera camera;

    public Camera getCamera() {
        return camera;
    }

    public FitViewport getViewPort() {
        return viewPort;
    }

    private ScreenManager() {
    }

    void init(HungerGame hg, SpriteBatch batch){
        this.game = hg;
        this.batch = batch;
        camera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        camera.position.set(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2, 0);
        camera.update();
        viewPort = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, camera);
        gs = new GameScreen(batch);
        ms = new MenuScreen(batch);
        ls = new LoadingScreen(batch);
    }

    public void resize(int x, int y){
        viewPort.update(x, y);
        viewPort.apply();
    }

    public void changeScreen(ScreenType type){
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        Gdx.input.setInputProcessor(null);
        if (screen != null) screen.dispose();

//        batch.setProjectionMatrix(camera.combined);
        game.setScreen(ls);
        switch (type){
            case MENU:
                targetScreen = ms;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gs;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
        }
    }

    public void gotoTarget(){
        game.setScreen(targetScreen);
    }
}
