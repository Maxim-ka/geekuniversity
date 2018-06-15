package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hunger.game.units.Food;
import com.hunger.game.units.Hero;
import com.hunger.game.units.MiniMap;
import com.hunger.game.units.Waste;

public class GameScreen implements Screen {

    private static final int NUMBER_FOODS = 90;
    private static final int NUMBER_HOOLIGANS = 30;
    private SpriteBatch batch;
    private Hero hero;
    private EnemyEmitter hooligans;
    private FoodEmitter foods;
    private WasteEmitter waste;
    private MiniMap miniMap;
    private BitmapFont font;
    private FitViewport viewPort;
    private Camera camera;
    private Music music;
    private Music heroReCreation;
    private float locationFontX;
    private float locationFontY;
    private boolean pause;

    public BitmapFont getFont() {
        return font;
    }

    public Music getHeroReCreation() {
        return heroReCreation;
    }

    public Music getMusic() {
        return music;
    }

    public MiniMap getMiniMap() {
        return miniMap;
    }

    public Camera getCamera() {
        return camera;
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
        camera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        viewPort = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, camera);
        locationFontX = viewPort.getWorldWidth() / 2 - Rules.INDENT;
        locationFontY = viewPort.getWorldHeight() / 2 - Rules.INDENT;
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

    private void onPause(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = !pause;
        }
    }

    @Override
    public void render(float delta) {
        onPause();
        if (pause){
            if (heroReCreation.isPlaying()) heroReCreation.pause();
            music.pause();
            batch.begin();
            font.draw(batch, "PAUSE",  camera.position.x - 50.0f,  camera.position.y + font.getCapHeight() / 2);
            batch.end();
            return;
        }
        if (!music.isPlaying()) music.play();
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        foods.render(batch);
        hero.render(batch);
        hooligans.render(batch);
        waste.render(batch);
        miniMap.render(batch);
        font.draw(batch, hero.getScore(),  camera.position.x - locationFontX,  camera.position.y + locationFontY);
        batch.end();
    }

    private void update(float dt){
        hero.update(dt);
        camera.position.set(hero.getPosition().x, hero.getPosition().y, 0);
        camera.update();
        hooligans.update(dt);
        foods.update(dt);
        waste.update(dt);
        miniMap.update(dt);
        checkForEatingFood();
        checkContact();
        checkForEatingAnother();
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
            if (hero.isRunOver(foods.activeList.get(j))){
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
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Assets.getInstance().clear();
    }
}
