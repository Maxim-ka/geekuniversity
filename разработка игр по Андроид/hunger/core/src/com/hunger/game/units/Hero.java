package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hunger.game.Assets;
import com.hunger.game.GameScreen;
import com.hunger.game.Joystick;
import com.hunger.game.Rules;

public class Hero extends Eater{

    private static final float TRANSITION_TIME_OF_LEVEL = 2.0f;
    private final StringBuilder scoreLine = new StringBuilder(Rules.LEVEL);
    private transient TextureRegion beaten;
    private transient TextureRegion hero;
    private float reCreationTime = 5.0f;
    private float transitionTime = TRANSITION_TIME_OF_LEVEL;
    private boolean atThatLevel = true;
    private boolean fatty;
    private boolean lose;
    private float fat;
    private int score;
    private transient Joystick joystick;

    public float getReCreationTime() {
        return reCreationTime;
    }

    public int getScore() {
        return score;
    }

    public boolean isFatty() {
        return fatty;
    }

    public boolean isAtThatLevel() {
        return atThatLevel;
    }

    public StringBuilder getScoreLine() {
        return scoreLine;
    }

    public Hero(GameScreen gs, Joystick joystick) {
        super(gs, "hero");
        super.init();
        this.joystick = joystick;
        beaten = Assets.getInstance().getAtlas().findRegion("beaten");
        angle = -90.0f;
        hero = region;
        acceleration = 300.0f;
    }

    @Override
    public void init(){
        scale = Rules.SCALE_EATER;
        region = hero;
        reCreationTime = 5.0f;
        comeToLevel();
        angle = -90.0f;
    }
    @Override
    public void render(SpriteBatch batch){
        super.render(batch);
        if (!isActive() && gs.getLive() > 0){
            gs.getFont().draw(batch, "to be continued...",  position.x - 150.0f,  position.y - halfHeight *scale);
        }
    }

    @Override
    public void gorge(GamePoint another){
        if (fat < 0) fat += another.satiety;
        else scale += another.satiety;
        if (this.scale <= Rules.MIN_SCALE) this.scale = Rules.MIN_SCALE;
        another.active = false;
        if (scale - Rules.SCALE_EATER > Rules.MAX_SCALE){
            fat += scale - Rules.SCALE_EATER;
            velocity.setZero();
            fatty = true;
        }
        if (fat >= Rules.MAX_SCALE && scale < Rules.SCALE_EATER){
            fat -= Rules.MAX_SCALE;
            velocity.setZero();
            lose = true;
        }
    }

    private boolean isMovedToLevel(float dt){
        atThatLevel = false;
        transitionTime -= dt;
        if (transitionTime > 0.0f) {
            int zoom = (fatty) ? -1 : 1;
            this.angle += zoom * 360.0f / TRANSITION_TIME_OF_LEVEL * dt;
            scale += zoom * (Rules.MAX_SCALE - Rules.SCALE_EATER) / TRANSITION_TIME_OF_LEVEL * dt;
            return true;
        }
        return false;
    }

    private void comeToLevel(){
        gs.toLevel();
        gs.getLandscape().initMapLevel();
        super.init();
        transitionTime = TRANSITION_TIME_OF_LEVEL;
        atThatLevel = true;
    }
    @Override
    public void update(float dt){
        if (isActive()) {
            super.update(dt);
            if (fatty){
                if (isMovedToLevel(dt)) return;
                else{
                    scale = Rules.SCALE_EATER;
                    comeToLevel();
                    fatty = false;
                }
            }
            if (lose){
                if (isMovedToLevel(dt)) return;
                else{
                    scale = Rules.MAX_SCALE;
                    comeToLevel();
                    lose = false;
                }
            }
            score = Math.round((scale - Rules.SCALE_EATER + fat) * 100);
            scoreLine.delete(Rules.LEVEL.length(), scoreLine.length());
            scoreLine.append(gs.getLevel()).append(Rules.LB).append(Rules.SCORE).append(score);
        } else{
            reCreationTime -= dt;
            if (reCreationTime > 0.0f){
                if (region != beaten){
                    region = beaten;
                    fat -= scale;
                    scale = (scale < Rules.SCALE_EATER) ? Rules.SCALE_EATER : scale;
                    velocity.setZero();
                    angle = -90.0f;
                    gs.getMusic().pause();
                    gs.getHeroReCreation().play();
                    gs.getHeroReCreation().setVolume(0.2f);
                    atThatLevel = false;
                }
                return;
            }
            init();
        }
        if (isAtThatLevel() && (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))){
            target.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewPortHero().unproject(target);
            angleToTarget = target.sub(position).angle();
            getSpeed(dt);
        }
    }

    public void setLoadedHero(GameScreen gs, Joystick joystick){
        this.gs = gs;
        this.joystick = joystick;
        beaten = Assets.getInstance().getAtlas().findRegion("beaten");
        hero = Assets.getInstance().getAtlas().findRegion("hero");
        region = (isActive()) ? hero : beaten;
    }
}
