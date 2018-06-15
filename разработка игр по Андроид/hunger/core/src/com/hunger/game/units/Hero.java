package com.hunger.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class Hero extends Eater{

    private final StringBuilder score = new StringBuilder(Rules.SCORE);
    private float time = 5.0f;
    private MiniHero miniHero;

    public StringBuilder getScore() {
        return score;
    }

    public Hero(GameScreen gs) {
        super(gs, "hero");
        position.set(Rules.GLOBAL_WIDTH / 2, Rules.GLOBAL_HEIGHT / 2);
        acceleration = 300.0f;
        angle = -90.0f;
        active = true;
        miniHero = new MiniHero(gs);
    }

    public void init(){
        super.init();
        angle = -90.0f;
        time = 5.0f;
    }

    private void setPositionMiniHero(){
        if (miniHero.active = active){
            miniHero.position.set(gs.getMiniMap().getPositionMini(this.position));
        }
    }

    public void render(SpriteBatch batch){
        if (isActive()){
            super.render(batch);
            miniHero.render(batch);
        }else{
            gs.getFont().draw(batch, "to be continued...",  position.x - 150.0f,  position.y + gs.getFont().getLineHeight());
        }
    }

    public void update(float dt){
        if (isActive()) {
            super.update(dt);
            setPositionMiniHero();
            score.delete(Rules.SCORE.length(), score.length());
            score.append(Math.round((scale - Rules.SCALE_EATER) * 100));
        } else{
            time -= dt;
            if (time > 0.0f){
                gs.getMusic().pause();
                gs.getHeroReCreation().play();
                gs.getHeroReCreation().setVolume(0.2f);
                return;
            }
            init();
        }
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            target.set(Gdx.input.getX(), Gdx.input.getY());
            gs.getViewPort().unproject(target);
            tmp.set(target);
            angleToTarget = tmp.sub(position).angle();
            getSpeed(dt);
        }
    }

    private class MiniHero extends GamePoint{
        MiniHero(GameScreen gs) {
            super(gs, "miniHero");
        }

        public void render(SpriteBatch batch){
            super.render(batch);
        }
    }
}
