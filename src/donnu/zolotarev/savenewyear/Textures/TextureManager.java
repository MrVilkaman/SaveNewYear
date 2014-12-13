package donnu.zolotarev.savenewyear.Textures;

import android.content.Context;
import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

public class TextureManager {


    private static final String GAME_BG_TEXTURE = "background.jpg";
    private static final String GAME_FG_TEXTURE = "frontground.png";
    private static final String GAME_ROAD_TEXTURE = "road.png";

    private static BitmapTextureAtlas gameBGTexture;
    private static BitmapTextureAtlas gameFGTexture;
    private static BitmapTextureAtlas testTexture;

    private static TextureRegion gameBG;
    private static TextureRegion gameFG;
    private static TextureRegion road;

    public static void initTextures(Context context, Engine engine){
        org.andengine.opengl.texture.TextureManager tm = engine.getTextureManager();

        gameBGTexture = new BitmapTextureAtlas(tm, 2048, 617, TextureOptions.BILINEAR);
        gameBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 0, 0);

        gameFGTexture = new BitmapTextureAtlas(tm, 2048, 242, TextureOptions.BILINEAR);
        gameFG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameFGTexture, context, GAME_FG_TEXTURE, 0, 0);

        testTexture = new BitmapTextureAtlas(tm, 512, 512, TextureOptions.BILINEAR);
        road = BitmapTextureAtlasTextureRegionFactory.createFromAsset(testTexture, context, GAME_ROAD_TEXTURE, 0, 0);

    }

    public static void loadGameSprites(){
        gameFGTexture.load();
        gameBGTexture.load();
        testTexture.load();
    }

    public static void unloadGameSprites(){
        gameFGTexture.unload();
        testTexture.unload();
        gameBGTexture.unload();
    }

    public static void clear(){
        testTexture.clearTextureAtlasSources();
        gameFGTexture.clearTextureAtlasSources();
        gameBGTexture.clearTextureAtlasSources();
    }

    public static TextureRegion getGameBG() {
        return gameBG;
    }

    public static TextureRegion getGameFG() {
        return gameFG;
    }

    public static TextureRegion getRoad() {
        return road;
    }
}
