package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hunger.game.units.Hero;
import com.hunger.game.units.MiniMap;
import com.hunger.game.units.Waste;

public class GameScreen implements Screen {

    private static final int NUMBER_FOODS = 90;
    private static final int NUMBER_HOOLIGANS = 20;
    private SpriteBatch batch;
    private Hero hero;
    private EnemyEmitter hooligans;
    private FoodEmitter foods;
    private WasteEmitter waste;
    private MiniMap miniMap;
    private BitmapFont font;
    private FitViewport viewPort;
    private OrthographicCamera cameraHero;
    private Music music;
    private Music heroReCreation;
    private Stage controlPanel;
    private boolean pause;

    public OrthographicCamera getCameraHero() {
        return cameraHero;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Music getHeroReCreation() {
        return heroReCreation;
    }

    public Music getMusic() {
        return music;
    }

    public FitViewport getViewPort() {
        return viewPort;
    }

    public Hero getHero() {
        return hero;
    }

    public EnemyEmitter getHooligans() {
        return hooligans;
    }

    public WasteEmitter getWaste() {
        return waste;
    }

    public FoodEmitter getFoods() {
        return foods;
    }

    GameScreen(SpriteBatch batch){
        this.batch = batch;
    }

    @Override
    public void show() {
        font = Assets.getInstance().getAssetManager().get("gomarice48.ttf");
        cameraHero = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        viewPort = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, cameraHero);
        installControlPanel();
        hero = new Hero(this);
        hooligans = new EnemyEmitter(this, NUMBER_HOOLIGANS);
        foods = new FoodEmitter(this, NUMBER_FOODS);
        waste = new WasteEmitter(this);
        miniMap = new MiniMap(this);
        heroReCreation = Assets.getInstance().getAssetManager().get("to_be_continued.mp3");
        music = Assets.getInstance().getAssetManager().get("Beverly_hills_COP_1984.mp3");
        music.setLooping(true);
        music.play();
        music.setVolume(0.1f);
    }

    private void installControlPanel(){
        controlPanel = new Stage(ScreenManager.getInstance().getViewPort(), batch);
        Gdx.input.setInputProcessor(controlPanel);
        Skin skin = new Skin(Assets.getInstance().getAtlas());

        Button.ButtonStyle stylePausePlay = new Button.ButtonStyle();
        stylePausePlay.up = skin.getDrawable("pause");
        stylePausePlay.over = skin.getDrawable("play");
        skin.add("stylePausePlay", stylePausePlay);

        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        styleExit.up = skin.getDrawable("exit");
        skin.add("styleExit", styleExit);

        Button buttonExitGame = new Button(skin, "styleExit");
        Button buttonPauseGame = new Button(skin, "stylePausePlay");

        buttonExitGame.setPosition(Rules.WORLD_WIDTH - styleExit.up.getMinWidth() - Rules.INDENT, Rules.WORLD_HEIGHT  - styleExit.up.getMinHeight() - Rules.INDENT);
        buttonPauseGame.setPosition(Rules.WORLD_WIDTH - stylePausePlay.up.getMinWidth() - Rules.INDENT, Rules.WORLD_HEIGHT - styleExit.up.getMinHeight() - 2 * Rules.INDENT - stylePausePlay.up.getMinHeight());

        controlPanel.addActor(buttonExitGame);
        controlPanel.addActor(buttonPauseGame);

        buttonPauseGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause = !pause;
            }
        });

        buttonExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

    }

    private void onPause(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = !pause;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraHero.combined);
        batch.begin();
        foods.render(batch);
        hero.render(batch);
        hooligans.render(batch);
        waste.render(batch);
        batch.end();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        miniMap.render(batch);
        font.draw(batch, hero.getScore(), Rules.INDENT, Rules.WORLD_HEIGHT - Rules.INDENT);
        if (pause){
            font.draw(batch, "PAUSE",  ScreenManager.getInstance().getCamera().position.x - 50.0f,  ScreenManager.getInstance().getCamera().position.y + font.getCapHeight() / 2);
        }
        batch.end();
        controlPanel.draw();
    }

    private void update(float dt){
        onPause();
        if (pause){
            if (heroReCreation.isPlaying()) heroReCreation.pause();
            music.pause();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            return;
        }
        controlPanel.act(dt);
        if (!music.isPlaying()) music.play();
        hero.update(dt);
        cameraHero.position.set(hero.getPosition().x, hero.getPosition().y, 0);
        cameraHero.update();
        if (hero.isAtThatLevel()){
            hooligans.update(dt);
            foods.update(dt);
            waste.update(dt);
            miniMap.update(dt);
            checkForEatingFood();
            checkContact();
            checkForEatingAnother();
        }else goToLevel();
    }

    private void goToLevel(){
        for (int i = 0; i < foods.activeList.size(); i++) {
            foods.free(i);
        }
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            hooligans.free(i);
        }
        for (int i = 0; i < waste.activeList.size(); i++) {
            waste.free(i);
        }
    }

    private void checkContact(){
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            for (int j = 0; j < waste.activeList.size() ; j++) {
                switch (waste.activeList.get(j).getType()){
                    case THORN:
                        waste.activeList.get(j).checkCollision(hero);
                        break;
                    case CORPSE:
                        waste.activeList.get(j).checkCollision(hooligans.activeList.get(i));
                        break;
                    case DETONATION:
                        waste.activeList.get(j).checkCollision(hero);
                        waste.activeList.get(j).checkCollision(hooligans.activeList.get(i));
                }
            }
        }
    }

    private void checkForEatingAnother(){
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            if (hooligans.activeList.get(i).isRunOver(hero)) {
                hooligans.activeList.get(i).smite(hero);
                if (!hooligans.activeList.get(i).isActive()) {
                    waste.getActiveElement().init(Waste.Type.CORPSE, hooligans.activeList.get(i));
                }
            }
        }
    }

    private void checkForEatingFood(){
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            for (int j = 0; j < foods.activeList.size(); j++) {
                if (hooligans.activeList.get(i).isRunOver(foods.activeList.get(j))){
                    hooligans.activeList.get(i).gorge(foods.activeList.get(j));
                    if (hooligans.activeList.get(i).getScale() > Rules.SCALE_EATER)
                        waste.getActiveElement().init(Waste.Type.THORN, foods.activeList.get(j));
                }
            }
        }
        for (int j = 0; j < foods.activeList.size(); j++) {
            if (hero.isActive() && hero.isRunOver(foods.activeList.get(j))){
                hero.gorge(foods.activeList.get(j));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
        viewPort.apply();
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        controlPanel.dispose();
        Assets.getInstance().clear();
    }
}
