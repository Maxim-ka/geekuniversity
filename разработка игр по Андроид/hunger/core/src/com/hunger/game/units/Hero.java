package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hunger.game.Assets;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class Hero extends Eater{

    private final StringBuilder score = new StringBuilder(Rules.SCORE);
    private final TextureRegion beaten = Assets.getInstance().getAtlas().findRegion("beaten");
    private float reCreationTime = 5.0f;
    private float transitionTime = 4.0f;
    private boolean atThatLevel = true;
    private float fat;
    private int levelConditions;
    private TextureRegion hero;

    public int getLevelConditions() {
        return levelConditions;
    }

    public boolean isAtThatLevel() {
        return atThatLevel;
    }

    public StringBuilder getScore() {
        return score;
    }

    public Hero(GameScreen gs) {
        super(gs, "hero");
        hero = region;
        position.set(Rules.GLOBAL_WIDTH / 2, Rules.GLOBAL_HEIGHT / 2);
        acceleration = 300.0f;
        angle = -90.0f;
        active = true;
    }

    public void init(){
        super.init();
        region = hero;
        angle = -90.0f;
        reCreationTime = 5.0f;
    }

    public void render(SpriteBatch batch){
        super.render(batch);
        if (!isActive()){
            gs.getFont().draw(batch, "to be continued...",  position.x - 150.0f,  position.y - halfHeight *scale);
        }
    }

    private boolean isMovedToLevel(float angle, float zoom, float dt){
        atThatLevel = false;
        transitionTime -= dt;
        if (transitionTime > 0.0f) {
            gs.getCameraHero().rotate(angle * dt);
            gs.getCameraHero().zoom += zoom * dt;
            return true;
        }
        return false;
    }

    private void comeToLevel(float zoom){
        transitionTime = 4.0f;
        gs.getCameraHero().zoom += transitionTime * zoom;
        levelConditions += 5;
        atThatLevel = true;
    }

    public void update(float dt){
        if (isActive()) {
            super.update(dt);
            if (halfWidth * scale > width){
                if (isMovedToLevel(180.0f, 2.0f, dt)) return;
                else{
                    fat += scale;
                    scale = Rules.SCALE_EATER;
                    comeToLevel(-2.0f);
                }
            }
            if (fat >= width / halfWidth && scale < Rules.SCALE_EATER){
                if (isMovedToLevel(-180.0f, -0.2f, dt)) return;
                else{
                    scale = width / halfWidth;
                    fat -= scale;
                    comeToLevel(0.2f);
                }
            }
            score.delete(Rules.SCORE.length(), score.length());
            score.append(Math.round((scale - Rules.SCALE_EATER + fat) * 100));
        } else{
            reCreationTime -= dt;
            if (reCreationTime > 0.0f){
                region = beaten;
                scale = (scale < Rules.SCALE_EATER) ? Rules.SCALE_EATER : scale;
                angle = -90.0f;
                fat = 0.0f;
                velocity.setZero();
                gs.getMusic().pause();
                gs.getHeroReCreation().play();
                gs.getHeroReCreation().setVolume(0.2f);
                return;
            }
            init();
        }
        if (isAtThatLevel() && (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))){
            target.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewPort().unproject(target);
            tmp.set(target);
            angleToTarget = tmp.sub(position).angle();
            getSpeed(dt);
        }
    }
}
