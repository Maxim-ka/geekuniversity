package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.hunger.game.units.Hero;
import com.hunger.game.units.MiniMap;
import com.hunger.game.units.Waste;

import java.io.*;

public class GameScreen implements Screen {

    private static final int NUMBER_FOODS = 30;
    private static final int NUMBER_HOOLIGANS = 15;
    private static final int NUMBER_PARTICLE = 200;
    private static final int QUANTITY_OF_LIFE = 5;
    private SpriteBatch batch;
    private boolean loadSaveGame;
    private Landscape landscape;
    private Hero hero;
    private EnemyEmitter hooligans;
    private FoodEmitter foods;
    private WasteEmitter waste;
    private ParticleEmitter particle;
    private TextureRegion life;
    private MiniMap miniMap;
    private Joystick joystick;
    private BitmapFont font;
    private FitViewport viewPortHero;
    private Camera cameraHero;
    private Music music;
    private Music heroReCreation;
    private Skin skin;
    private Stage controlPanel;
    private Dialog dialog;
    private Stage stageDialog;
    private boolean pause;
    private boolean exit;
    private int level;
    private int live;
    private int score;

    int getScore() {
        return score;
    }

    public int getLive() {
        return live;
    }

    public ParticleEmitter getParticle() {
        return particle;
    }

    void setLoadSaveGame(boolean loadSaveGame) {
        this.loadSaveGame = loadSaveGame;
    }

    public int getLevel() {
        return level;
    }

    public Landscape getLandscape() {
        return landscape;
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

    public FitViewport getViewPortHero() {
        return viewPortHero;
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
        cameraHero = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        viewPortHero = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, cameraHero);
        font = Assets.getInstance().getAssetManager().get("gomarice48.ttf");
        joystick = new Joystick();
        if (loadSaveGame){
            loadGame();
        }else {
            level = 0;
            live = QUANTITY_OF_LIFE;
            landscape = new Landscape(this);
            hero = new Hero(this, joystick);
            foods = new FoodEmitter(this, NUMBER_FOODS);
            hooligans = new EnemyEmitter(this, NUMBER_HOOLIGANS);
            waste = new WasteEmitter(this);
            particle = new ParticleEmitter(NUMBER_PARTICLE);
        }
        life = Assets.getInstance().getAtlas().findRegion("life");
        miniMap = new MiniMap(this);
        skin = new Skin(Assets.getInstance().getAtlas());
        installControlPanel();
        generateOutputDialog();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(controlPanel, stageDialog, joystick);
        Gdx.input.setInputProcessor(inputMultiplexer);
        heroReCreation = Assets.getInstance().getAssetManager().get("to_be_continued.mp3");
        music = Assets.getInstance().getAssetManager().get("Beverly_hills_COP_1984.mp3");
        music.setLooping(true);
        music.play();
        music.setVolume(0.1f);
    }

    private void generateOutputDialog(){
        BitmapFont font26 = Assets.getInstance().getAssetManager().get("gomarice26.ttf");
        BitmapFont font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf");
        skin.add("font26", font26);
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonDialog");
        textButtonStyle.font = skin.getFont("font26");
        skin.add("textButtonStyle", textButtonStyle);

        Button butSave = new TextButton("Yes", skin, "textButtonStyle");
        Button butExit = new TextButton("No", skin, "textButtonStyle");
        Button butCancel = new TextButton("Cancel", skin, "textButtonStyle");

        Label label = new Label("Save this game?", skin, "font32", Color.ORANGE);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font26, Color.YELLOW,
                skin.getDrawable("windowDialog"));

        stageDialog = new Stage(ScreenManager.getInstance().getViewPort(), batch);

        dialog = new Dialog(" Exit", windowStyle){
            @Override
            public void result(Object object){
                exit = false;
                pause = false;
                if ((int)object == 0){
                    dialog.hide();
                    selectRenderingPause();
                    return;
                }
                if ((int)object == 1){
                    recordScore();
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                    return;
                }
                if ((int)object == 2){
                    saveGame();
                    recordScore();
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                }
            }
        };
        dialog.text(label);
        dialog.button(butSave, 2);
        dialog.button(butExit, 1);
        dialog.button(butCancel, 0);
    }

    private void saveGame(){
        try(ObjectOutputStream save = new ObjectOutputStream(Gdx.files.local(Rules.SAVE_FILE)
                .write(false))) {
            save.writeInt(level);
            save.writeInt(live);
            save.writeObject(landscape);
            save.writeObject(hero);
            save.writeObject(foods);
            save.writeObject(hooligans);
            save.writeObject(waste);
            save.writeObject(particle);
            save.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGame(){
        try (ObjectInputStream load = new ObjectInputStream(Gdx.files.local(Rules.SAVE_FILE)
                .read())){
            level = load.readInt();
            live = load.readInt();
            landscape = (Landscape) load.readObject();
            landscape.setLoadedLandscape(this);
            hero = (Hero) load.readObject();
            hero.setLoadedHero(this, joystick);
            foods = (FoodEmitter) load.readObject();
            foods.setLoadedFoodEmitter(this);
            hooligans = (EnemyEmitter) load.readObject();
            hooligans.setLoadedEnemyEmitter(this);
            waste = (WasteEmitter) load.readObject();
            waste.setLoadedWaste(this);
            particle = (ParticleEmitter) load.readObject();
            particle.setLoadedParticleEmitter();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void installControlPanel(){
        controlPanel = new Stage(ScreenManager.getInstance().getViewPort(), batch);

        Button.ButtonStyle stylePausePlay = new Button.ButtonStyle();
        String image = (loadSaveGame) ? Rules.PLAY : Rules.PAUSE;
        stylePausePlay.up = skin.getDrawable(image);
        skin.add("stylePausePlay", stylePausePlay);

        Button.ButtonStyle styleExit = new Button.ButtonStyle();
        styleExit.up = skin.getDrawable("exit");
        skin.add("styleExit", styleExit);

        Button butExitGame = new Button(skin, "styleExit");
        Button butPauseGame = new Button(skin, "stylePausePlay");

        butExitGame.setPosition(Rules.WORLD_WIDTH - styleExit.up.getMinWidth() - Rules.INDENT,
            Rules.WORLD_HEIGHT  - styleExit.up.getMinHeight() - Rules.INDENT);
        butPauseGame.setPosition(Rules.WORLD_WIDTH - stylePausePlay.up.getMinWidth() - Rules.INDENT,
            Rules.WORLD_HEIGHT - styleExit.up.getMinHeight() - 2 * Rules.INDENT - 2 * stylePausePlay.up.getMinHeight());

        controlPanel.addActor(butExitGame);
        controlPanel.addActor(butPauseGame);

        butPauseGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause = !pause;
                selectRenderingPause();
            }
        });

        butExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exit = true;
                pause = true;
                dialog.show(stageDialog);
            }
        });
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(cameraHero.combined);
        batch.begin();
        landscape.render(batch);
        foods.render(batch);
        hero.render(batch);
        hooligans.render(batch);
        waste.render(batch);
        joystick.render(batch);
        particle.render(batch);
        batch.end();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        if (!exit)controlPanel.draw();
        if (exit) stageDialog.draw();
        batch.begin();
        showLives(batch);
        miniMap.render(batch);
        font.draw(batch, hero.getScoreLine(), Rules.INDENT, Rules.WORLD_HEIGHT - Rules.INDENT);
        if (pause){
            font.draw(batch, Rules.PAUSE,  ScreenManager.getInstance().getCamera().position.x - 50.0f,
                ScreenManager.getInstance().getCamera().position.y + font.getCapHeight() / 2);
        }
        if (live == 0){
            font.draw(batch, "game over",  ScreenManager.getInstance().getCamera().position.x - 100.0f,
                ScreenManager.getInstance().getCamera().position.y + font.getCapHeight() / 2);
        }
        batch.end();
    }

    private void recordScore(){
        score = hero.getScore();
        if (Gdx.files.local(Rules.PATH_SCORE_HUNGER).exists()){
            try (DataInput input = new DataInput(Gdx.files.local(Rules.PATH_SCORE_HUNGER).read())){
                if (score > input.readInt()){
                    record();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            record();
        }
    }

    private void record(){
        try (DataOutput out = new DataOutput(Gdx.files.local(Rules.PATH_SCORE_HUNGER).write(false))){
            out.writeInt(score);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLives(SpriteBatch batch){
        if (live > 0){
            batch.setColor(1,1,1,0.8f);
            for (int i = 0; i < live; i++) {
                batch.draw(life, Rules.WORLD_WIDTH / 2.0f - (life.getRegionWidth() + Rules.INDENT) *
                    live / 2 + life.getRegionWidth() + (Rules.INDENT + life.getRegionWidth()) * i,
                        Rules.WORLD_HEIGHT - Rules.INDENT - life.getRegionHeight());
            }
            batch.setColor(1,1,1,1);
        }
    }

    public void toLevel(){
        level = (hero.isFatty()) ? ++level : (level != 0) ? --level : 0;
    }

    private void update(float dt){
        controlPanel.act(dt);
        stageDialog.act(dt);
        if (pause){
            if (heroReCreation.isPlaying()) heroReCreation.pause();
            music.pause();
            return;
        }
        if (hero.isActive() && !music.isPlaying()) music.play();
        if (live > 0)landscape.update(dt);
        hero.update(dt);
        if (live == 0 && hero.getReCreationTime() <= 1){
            recordScore();
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.OVER);
        }
        cameraHero.position.set(hero.getPosition().x, hero.getPosition().y, 0);
        controlCameraHero();
        cameraHero.update();
        if (hero.isAtThatLevel()){
            hooligans.update(dt);
            foods.update(dt);
            waste.update(dt);
            miniMap.update(dt);
            particle.update(dt);
            checkForEatingFood();
            checkContact();
            checkForEatingAnother();
            if (loadSaveGame){
                pause = true;
                loadSaveGame = false;
            }
            return;
        }
        goToLevel();
    }

    private void controlCameraHero(){
        if (cameraHero.position.x < Rules.WORLD_WIDTH / 2) cameraHero.position.x = Rules.WORLD_WIDTH / 2.0f;
        if (cameraHero.position.y < Rules.WORLD_HEIGHT / 2) cameraHero.position.y = Rules.WORLD_HEIGHT / 2.0f;
        if (cameraHero.position.x > Rules.GLOBAL_WIDTH - Rules.WORLD_WIDTH / 2)
            cameraHero.position.x = Rules.GLOBAL_WIDTH - Rules.WORLD_WIDTH / 2.0f;
        if (cameraHero.position.y > Rules.GLOBAL_HEIGHT - Rules.WORLD_HEIGHT / 2)
            cameraHero.position.y = Rules.GLOBAL_HEIGHT - Rules.WORLD_HEIGHT / 2.0f;
    }

    private void goToLevel(){
        foods.toLeaveLevel();
        hooligans.toLeaveLevel();
        waste.toLeaveLevel();
        particle.toLeaveLevel();
    }

    private void checkContact(){
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            for (int j = 0; j < waste.activeList.size() ; j++) {
                switch (waste.activeList.get(j).getType()){
                    case THORN:
                        waste.activeList.get(j).checkCollision(hero);
                        if (!hero.isActive()){
                            live--;
                            return;
                        }
                        break;
                    case CORPSE:
                        waste.activeList.get(j).checkCollision(hooligans.activeList.get(i));
                }
            }
        }
        for (int j = 0; j < particle.activeList.size() ; j++) {
            particle.activeList.get(j).toGetTo(hero);
            if (!hero.isActive()){
                live--;
                return;
            }
            for (int i = 0; i < hooligans.activeList.size(); i++)
                particle.activeList.get(j).toGetTo(hooligans.activeList.get(i));
        }
    }

    private void checkForEatingAnother(){
        for (int i = 0; i < hooligans.activeList.size(); i++) {
            if (hooligans.activeList.get(i).isRunOver(hero) || hero.isRunOver(hooligans.activeList.get(i))) {
                hooligans.activeList.get(i).smite(hero);
                if (!hero.isActive()) {
                    live--;
                    return;
                }
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
        ScreenManager.getInstance().resize(width, height);
        viewPortHero.update(width, height);
        viewPortHero.apply();
    }

    @Override
    public void pause() {
        pause = true;
    }

    private void selectRenderingPause(){
        Button buttonPause = (Button) controlPanel.getActors().get(1);
        Button.ButtonStyle style = buttonPause.getStyle();
        String image = (pause) ? Rules.PLAY : Rules.PAUSE;
        style.up = skin.getDrawable(image);
        buttonPause.setStyle(style);
    }

    @Override
    public void resume() {
        pause = true;
        selectRenderingPause();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stageDialog.dispose();
        controlPanel.dispose();
        Assets.getInstance().clear();
    }
}
