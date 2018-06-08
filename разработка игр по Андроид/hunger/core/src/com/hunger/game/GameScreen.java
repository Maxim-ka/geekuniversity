package com.hunger.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.hunger.game.units.Food;
import com.hunger.game.units.Hero;
import com.hunger.game.units.Waste;

public class GameScreen implements Screen {

    private static final int NUMBER_FOODS = 10;
    private static final int NUMBER_ENEMIES = 4;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Hero hero;
    private EnemyEmitter enemies;
    private FoodEmitter foods;
    private WasteEmitter waste;

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Hero getHero() {
        return hero;
    }

    public EnemyEmitter getEnemies() {
        return enemies;
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
        atlas = new TextureAtlas("hunger_1.pack");
        hero = new Hero(this);
        enemies = new EnemyEmitter(this, NUMBER_ENEMIES);
        foods = new FoodEmitter(this, NUMBER_FOODS);
        waste = new WasteEmitter(this);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        foods.render(batch);
        hero.render(batch);
        enemies.render(batch);
        waste.render(batch);
        batch.end();
    }

    private void update(float dt){
        hero.update(dt);
        enemies.update(dt);
        foods.update(dt);
        waste.update(dt);
        checkForEating();
        checkContact();
    }

    private void checkContact(){
        for (int j = 0; j < waste.activeList.size(); j++) {
            if (waste.activeList.get(j).getType() == Waste.Type.THORN)
                waste.activeList.get(j).checkCollision(hero);
            for (int i = 0; i < enemies.activeList.size(); i++) {
                if (waste.activeList.get(j).getType() == Waste.Type.CORPSE)
                    waste.activeList.get(j).checkCollision(enemies.activeList.get(i));
            }
        }
    }

    private void checkForEating(){
        for (int i = 0; i < enemies.activeList.size(); i++) {
            if (enemies.activeList.get(i).isRunOver(hero)){
                enemies.activeList.get(i).smite(hero);
                if (!enemies.activeList.get(i).isActive()){
                    waste.getCorpse().init(enemies.activeList.get(i));
                    enemies.checkPool();
                    continue;
                }
            }
            for (int j = 0; j < foods.activeList.size(); j++) {
                if (enemies.activeList.get(i).isRunOver(foods.activeList.get(j))){
                    enemies.activeList.get(i).gorge(foods.activeList.get(j));
                    if (enemies.activeList.get(i).getScale() > Rules.SCALE_EATER && foods.activeList.get(j).getType() != Food.Type.LEMON)
                        waste.getThorn().init(foods.activeList.get(j));
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
        atlas.dispose();
    }
}
