package donnu.zolotarev.savenewyear.Textures;

import android.content.Context;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Textures.Ids.GameTextureId_1;
import donnu.zolotarev.savenewyear.Textures.Ids.MenuTextures;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;
import org.andengine.util.texturepack.exception.TexturePackParseException;

public class TextureManager {


    private static final String GAME_BG_TEXTURE = "background.jpg";
    private static final String GAME_FG_TEXTURE = "frontground.png";
    private static final String MENU_BG_TEXTURE = "menubackground.jpg";

    private static BitmapTextureAtlas gameBGTexture;
    private static BitmapTextureAtlas gameFGTexture;
    private static BitmapTextureAtlas menuBGTexture;
    private static TexturePack texturePack1;
    private static TexturePack texturePack2;

    private static Font font;
    private static IFont bigFont;

    private static TextureRegion menuBG;
    private static TextureRegion gameBG;
    private static TextureRegion gameFG;
    private static ITiledTextureRegion road;
    private static ITiledTextureRegion hero;
    private static ITiledTextureRegion buttons;
    private static ITiledTextureRegion gameTitle;
    private static ITiledTextureRegion pauseButton;
    private static ITiledTextureRegion showBalls;
    private static ITiledTextureRegion present;
    private static ITiledTextureRegion waterHoll;
    private static ITiledTextureRegion newYearTree;
    private static ITiledTextureRegion heroShedow;
    private static ITiledTextureRegion particlePoint;
    private static ITiledTextureRegion tree;
    private static ITiledTextureRegion googlePlay;


    public static void initTextures(Context context, Engine engine){
        org.andengine.opengl.texture.TextureManager tm = engine.getTextureManager();

        gameBGTexture = new BitmapTextureAtlas(tm, 2048, 617, TextureOptions.BILINEAR);
        gameBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameBGTexture, context, GAME_BG_TEXTURE, 0, 0);

        gameFGTexture = new BitmapTextureAtlas(tm, 2048, 242, TextureOptions.BILINEAR);
        gameFG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameFGTexture, context, GAME_FG_TEXTURE, 0, 0);

        menuBGTexture = new BitmapTextureAtlas(tm, 1280, 768, TextureOptions.BILINEAR);
        menuBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBGTexture, context, MENU_BG_TEXTURE, 0, 0);

        try {
            texturePack1 = new TexturePackLoader(context.getAssets(),tm).loadFromAsset("gfx/GameTextureId_1.xml", "gfx/");
            TexturePackTextureRegionLibrary lib = texturePack1.getTexturePackTextureRegionLibrary();
            road = lib.getTiled(GameTextureId_1.ROAD_ID);
            hero = lib.getTiled(GameTextureId_1.HERO_ID,2,2);
            showBalls = lib.getTiled(GameTextureId_1.SHOWBALLS_ID,2,1);
            present = lib.getTiled(GameTextureId_1.PRESENT_ID);
            waterHoll = lib.getTiled(GameTextureId_1.WATER_HOLL_ID);
            newYearTree = lib.getTiled(GameTextureId_1.NEW_YEAR_TREE_ID);
            heroShedow = lib.getTiled(GameTextureId_1.HERO_SHEDOW_ID);
            tree = lib.getTiled(GameTextureId_1.TREE_ID,8,1);

        } catch (TexturePackParseException e) {
            e.printStackTrace();
        }

        try {
            texturePack2 = new TexturePackLoader(context.getAssets(),tm).loadFromAsset("gfx/MenuTextures.xml", "gfx/");
            TexturePackTextureRegionLibrary lib = texturePack2.getTexturePackTextureRegionLibrary();
            buttons = lib.getTiled(MenuTextures.BUTTONS_ID,1,2);
            if (context.getResources().getString(R.string.localizade).equals("RUS")) {
                gameTitle = lib.getTiled(MenuTextures.GAMENAMERUS_ID);
            }else{
                gameTitle = lib.getTiled(MenuTextures.GAMENAME_ID);
            }
            pauseButton = lib.getTiled(MenuTextures.PAUSE_BUTTON_ID);
            particlePoint = lib.getTiled(MenuTextures.PARTICLE_POINT_ID);
            googlePlay = lib.getTiled(MenuTextures.GOOGLE_PLAY_ID);
        } catch (TexturePackParseException e) {
            e.printStackTrace();
        }

        font = FontFactory.createFromAsset(engine.getFontManager(), tm, 256, 256, context.getAssets(), "gfx/BuxtonSketch.ttf", 64, true,
                Color.WHITE_ABGR_PACKED_INT);
        bigFont = FontFactory.createFromAsset(engine.getFontManager(), tm, 512, 512, context.getAssets(), "gfx/BuxtonSketchNumbers.ttf", 128, true,
                Color.WHITE_ABGR_PACKED_INT);

        bigFont.load();
        font.load();
    }

    public static void loadGameSprites(){
        gameFGTexture.load();
        gameBGTexture.load();
        texturePack1.loadTexture();
    }

    public static void unloadGameSprites(){
        gameFGTexture.unload();
        gameBGTexture.unload();
        texturePack1.unloadTexture();

    }
    public static void loadMenuSprites() {
        menuBGTexture.load();
        texturePack2.loadTexture();
    }

    public static void clear(){
        gameFGTexture.unload();
        gameBGTexture.unload();
        menuBGTexture.unload();
        texturePack1.unloadTexture();
        texturePack2.unloadTexture();
        gameFGTexture.clearTextureAtlasSources();
        gameBGTexture.clearTextureAtlasSources();
        menuBGTexture.clearTextureAtlasSources();
        bigFont.unload();
        font.unload();
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

    public static ITiledTextureRegion getButtons() {
        return buttons;
    }

    public static ITiledTextureRegion getGameTitle() {
        return gameTitle;
    }

    public static TextureRegion getMenuBG() {
        return menuBG;
    }

    public static Font getFont() {
        return font;
    }

    public static ITiledTextureRegion getPauseButton() {
        return pauseButton;
    }

    public static ITiledTextureRegion getShowBalls() {
        return showBalls;
    }

    public static ITiledTextureRegion getPresent() {
        return present;
    }

    public static ITiledTextureRegion getWaterHoll() {
        return waterHoll;
    }

    public static ITiledTextureRegion getNewYearTree() {
        return newYearTree;
    }

    public static IFont getBigFont() {
        return bigFont;
    }

    public static ITiledTextureRegion getHeroShedow() {
        return heroShedow;
    }

    public static ITiledTextureRegion getParticlePoint() {
        return particlePoint;
    }

    public static ITiledTextureRegion getTree() {
        return tree;
    }
    public static ITiledTextureRegion getGooglePlay() {
        return googlePlay;
    }
}
