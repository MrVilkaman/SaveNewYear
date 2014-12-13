package donnu.zolotarev.savenewyear.Textures;

import android.content.Context;
import donnu.zolotarev.savenewyear.Textures.Ids.GameTextureId_1;
import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;
import org.andengine.util.texturepack.exception.TexturePackParseException;

public class TextureManager {


    private static final String GAME_BG_TEXTURE = "background.jpg";
    private static final String GAME_FG_TEXTURE = "frontground.png";
    private static final String GAME_ROAD_TEXTURE = "road.png";

    private static BitmapTextureAtlas gameBGTexture;
    private static BitmapTextureAtlas gameFGTexture;
    private static TexturePack texturePack1;

    private static TextureRegion gameBG;
    private static TextureRegion gameFG;
    private static ITiledTextureRegion road;
    private static ITiledTextureRegion hero;

    public static void initTextures(Context context, Engine engine){
        org.andengine.opengl.texture.TextureManager tm = engine.getTextureManager();

        gameBGTexture = new BitmapTextureAtlas(tm, 2048, 617, TextureOptions.BILINEAR);
        gameBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 0, 0);

        gameFGTexture = new BitmapTextureAtlas(tm, 2048, 242, TextureOptions.BILINEAR);
        gameFG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameFGTexture, context, GAME_FG_TEXTURE, 0, 0);


        try {
            texturePack1 = new TexturePackLoader(context.getAssets(),tm).loadFromAsset("gfx/GameTextureId_1.xml", "gfx/");
            TexturePackTextureRegionLibrary lib = texturePack1.getTexturePackTextureRegionLibrary();
            road = lib.getTiled(GameTextureId_1.ROAD_ID);
            hero = lib.getTiled(GameTextureId_1.HERO_ID,2,2);
        } catch (TexturePackParseException e) {
            e.printStackTrace();
        }

    }

    public static void loadGameSprites(){
        gameFGTexture.load();
        gameBGTexture.load();
        texturePack1.loadTexture();
    }

    public static void unloadGameSprites(){
        gameFGTexture.unload();
        gameBGTexture.unload();
    }

    public static void clear(){
        gameFGTexture.clearTextureAtlasSources();
        gameBGTexture.clearTextureAtlasSources();
    }

    public static TextureRegion getGameBG() {
        return gameBG;
    }

    public static TextureRegion getGameFG() {
        return gameFG;
    }

    public static ITiledTextureRegion getRoad() {
        return road;
    }

    public static ITiledTextureRegion getHero() {
        return hero;
    }
}
