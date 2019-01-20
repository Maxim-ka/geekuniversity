package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hunger.game.Assets;
import com.hunger.game.GameScreen;
import com.hunger.game.Joystick;
import com.hunger.game.Rules;

public class Hero extends Eater{

    private static final int TO_LEFT = 1;
    private static final int TO_RIGHT = -1;
    private static final float TRANSITION_TIME_OF_LEVEL = 2.0f;
    private static final float INDENT = 150.0f;
    private static final float VOLUME = 0.2f;
    private final StringBuilder scoreLine = new StringBuilder(Rules.LEVEL);
    private transient TextureRegion beaten;
    private transient TextureRegion hero;
    private float reCreationTime = 5.0f;
    private float transitionTime = TRANSITION_TIME_OF_LEVEL;
    private boolean atThatLevel;
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
        this.joystick = joystick;
        beaten = Assets.getInstance().getAtlas().findRegion("beaten");
        hero = region;
        init();
    }

    @Override
    public void init(){
        position.set(Rules.GLOBAL_WIDTH / 2.0f, Rules.GLOBAL_HEIGHT / 2.0f);
        scale = Rules.SCALE_EATER;
        region = hero;
        reCreationTime = 5.0f;
        angle = -90.0f;
        acceleration = 300.0f;
        gs.getLandscape().initMapLevel(this);
        atThatLevel = true;
        active = true;
    }

    @Override
    public void render(SpriteBatch batch){
        super.render(batch);
        if (!isActive() && gs.getLive() > 0){
            gs.getFont().draw(batch, "to be continued...",  position.x - INDENT,
                    position.y - halfHeight *scale);
        }
    }

    @Override
    public void gorge(GamePoint another){
        float boost;
        if (fat < 0){
            fat += another.satiety;
            boost = 1;
        } else {
            scale += another.satiety;
            boost = 10;
        }
        if (this.scale <= Rules.MIN_SCALE) this.scale = Rules.MIN_SCALE;
        acceleration += another.satiety * boost;
        another.active = false;
        if (scale - Rules.SCALE_EATER >= Rules.MAX_SCALE){
            fat += scale - Rules.SCALE_EATER;
            fatty = true;
            return;
        }
        if (fat >= Rules.MAX_SCALE && scale < Rules.SCALE_EATER){
            fat -= Rules.MAX_SCALE;
            lose = true;
        }
    }

    private boolean isMovedToLevel(float dt){
        velocity.setZero();
        atThatLevel = false;
        transitionTime -= dt;
        if (transitionTime > 0) {
            int zoom = (fatty) ? TO_RIGHT : TO_LEFT;
            this.angle += zoom * Rules.ANGLE_360_DEGREES / TRANSITION_TIME_OF_LEVEL * dt;
            scale += zoom * (Rules.MAX_SCALE - Rules.SCALE_EATER) / TRANSITION_TIME_OF_LEVEL * dt;
            return true;
        }
        return false;
    }

    private void comeToLevel(){
        position.set(Rules.GLOBAL_WIDTH / 2.0f, Rules.GLOBAL_HEIGHT / 2.0f);
        gs.toLevel();
        gs.getLandscape().initMapLevel(this);
        transitionTime = TRANSITION_TIME_OF_LEVEL;
        if (fatty) scale = Rules.SCALE_EATER;
        if (lose) scale = Rules.MAX_SCALE;
        atThatLevel = true;
    }

    @Override
    public void update(float dt){
        if (isActive()) {
            super.update(dt);
            if (fatty || lose){
                if (isMovedToLevel(dt)) return;
                else{
                    comeToLevel();
                    fatty = false;
                    lose = false;
                }
            }
            score = Math.round((scale - Rules.SCALE_EATER + fat) * 100);
            scoreLine.delete(Rules.LEVEL.length(), scoreLine.length());
            scoreLine.append(gs.getLevel()).append(Rules.LB).append(Rules.SCORE).append(score);
        } else{
            reCreationTime -= dt;
            if (reCreationTime > 0){
                if (region != beaten && isAtThatLevel()){
                    region = beaten;
                    fat -= scale;
                    scale = (scale < Rules.SCALE_EATER) ? Rules.SCALE_EATER : scale;
                    velocity.setZero();
                    angle = -Rules.ANGLE_90_DEGREES;
                    gs.getMusic().pause();
                    gs.getHeroReCreation().play();
                    gs.getHeroReCreation().setVolume(VOLUME);
                    atThatLevel = false;
                }
                return;
            }
            init();
        }
        if (isAtThatLevel() && Gdx.input.isTouched()){
            target.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewPortHero().unproject(target);
            angleToTarget = target.sub(position).angle();
            float velLen = getGoing(dt);
            for (int i = 0; i < velLen; i++) {
                tmp.set(position.x + nX, position.y + nY);
                if (isCollided()) return;
                position.set(tmp);
            }
        }
    }

    private boolean isCollided(){
        return gs.getLandscape()
                .collide(tmp.x, tmp.y, halfWidth * scale, angle) != Rules.NOT_FOUND;
    }

    public void setLoadedHero(GameScreen gs, Joystick joystick){
        this.gs = gs;
        this.joystick = joystick;
        beaten = Assets.getInstance().getAtlas().findRegion("beaten");
        hero = Assets.getInstance().getAtlas().findRegion("hero");
        region = (isActive()) ? hero : beaten;
    }
}
