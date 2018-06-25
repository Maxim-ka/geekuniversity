package com.hunger.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class Assets {

    private static Assets ourInstance = new Assets();
    private AssetManager assetManager;
    private TextureAtlas atlas;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public static Assets getInstance() {
        return ourInstance;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type){
        switch (type){
            case GAME:
                assetManager.load("hunger_game.pack", TextureAtlas.class);
                assetManager.load("to_be_continued.mp3", Music.class);
                assetManager.load("Beverly_hills_COP_1984.mp3", Music.class);
                createStdFont(26);
                createStdFont(32);
                createStdFont(48);
                break;
            case MENU:
                assetManager.load("menu_hunger.pack", TextureAtlas.class);
                createStdFont(26);
                createStdFont(32);
                createStdFont(92);
                break;
        }
    }

    public void makeLinks(){
        if (assetManager.isLoaded("menu_hunger.pack")){
            atlas = assetManager.get("menu_hunger.pack");
            return;
        }
        if (assetManager.isLoaded("hunger_game.pack"))
            atlas = assetManager.get("hunger_game.pack");
    }

    private void createStdFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.YELLOW;
        fontParameter.fontParameters.borderWidth = 2;
        fontParameter.fontParameters.borderColor = Color.BLACK;
        fontParameter.fontParameters.shadowOffsetX = 2;
        fontParameter.fontParameters.shadowOffsetY = 2;
        fontParameter.fontParameters.shadowColor = Color.BLACK;
        assetManager.load("gomarice" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    public void clear(){
        assetManager.clear();
    }
}
