package com.hunger.game.units;

import com.badlogic.gdx.math.Vector2;
import com.hunger.game.GameScreen;
import com.hunger.game.Rules;

public class MiniMap extends GamePoint {

    private GameScreen gs;
    private float locationX;
    private float locationY;
    private float relationX;
    private float relationY;
    private Vector2 tmp;

    public MiniMap(GameScreen gs) {
        super(gs, "miniMap");
        this.gs = gs;
        locationX = gs.getViewPort().getWorldWidth() / 2 - halfWidth - Rules.INDENT;
        locationY = gs.getViewPort().getWorldHeight() / 2 - halfHeight - Rules.INDENT;
        active = true;
        relationX = Rules.GLOBAL_WIDTH / width;
        relationY = Rules.GLOBAL_HEIGHT / height;
        tmp = new Vector2();
    }

    Vector2 getPositionMini(Vector2 pos){
        float originOfCoordinatesX = gs.getCamera().position.x + locationX - halfWidth;
        float originOfCoordinatesY = gs.getCamera().position.y - locationY - halfHeight;
        float x = originOfCoordinatesX + pos.x / relationX;
        if (x < originOfCoordinatesX) x = originOfCoordinatesX;
        if (x > originOfCoordinatesX + width)  x = originOfCoordinatesX + width;
        float y = originOfCoordinatesY + pos.y / relationY;
        if (y < originOfCoordinatesY) y = originOfCoordinatesY;
        if (y > originOfCoordinatesY + height) y = originOfCoordinatesY + height;
        return tmp.set(x, y);
    }

    @Override
    public void update(float dt) {
        this.position.set(gs.getCamera().position.x + locationX, gs.getCamera().position.y - locationY);
    }
}
