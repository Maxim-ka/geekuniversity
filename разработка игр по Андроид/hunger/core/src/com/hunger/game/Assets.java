package com.hunger.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    public void loadAssets(){
        assetManager.load("hunger_1.pack", TextureAtlas.class);
        assetManager.load("to_be_continued.mp3", Music.class);
        assetManager.load("Beverly_hills_COP_1984.mp3", Music.class);
        createStdFont(48);
        assetManager.finishLoading();
        atlas = assetManager.get("hunger_1.pack");
    }

    public void createStdFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.borderWidth = 2;
        fontParameter.fontParameters.borderColor = Color.BLACK;
        fontParameter.fontParameters.shadowOffsetX = 2;
        fontParameter.fontParameters.shadowOffsetY = 2;
        fontParameter.fontParameters.shadowColor = Color.GRAY;
        assetManager.load("gomarice" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    public void clear(){
        assetManager.clear();
    }
}
