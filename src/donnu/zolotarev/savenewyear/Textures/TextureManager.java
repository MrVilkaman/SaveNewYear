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

    private static BitmapTextureAtlas gameBGTexture;
    private static BitmapTextureAtlas gameFGTexture;

    private static TextureRegion gameBG;
    private static TextureRegion gameFG;

    public static void initTextures(Context context, Engine engine){
        org.andengine.opengl.texture.TextureManager tm = engine.getTextureManager();

        gameBGTexture = new BitmapTextureAtlas(tm, 2048, 617, TextureOptions.BILINEAR);
//        BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 0, 0);
//        BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 6, 0);
        gameBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 0, 0);

        gameFGTexture = new BitmapTextureAtlas(tm, 2048, 242, TextureOptions.BILINEAR);
//        BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_FG_TEXTURE, 0, 0);
//        BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_FG_TEXTURE, 6, 0);
        gameFG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameFGTexture, context, GAME_FG_TEXTURE, 0, 0);

    }

    public static void loadGameSprites(){
        gameFGTexture.load();
        gameBGTexture.load();
    }

    public static void unloadGameSprites(){
        gameFGTexture.unload();
        gameBGTexture.unload();
    }

    public static TextureRegion getGameBG() {
        return gameBG;
    }

    public static TextureRegion getGameFG() {
        return gameFG;
    }
}
