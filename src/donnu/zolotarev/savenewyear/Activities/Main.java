package donnu.zolotarev.savenewyear.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.Scenes.BaseScene;
import donnu.zolotarev.savenewyear.Scenes.MainMenuScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class Main extends SimpleBaseGameActivity {


        private Camera camera;
        private BaseScene mainMenu;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        GameContex.setGameActivity(this);
    }


    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0,0, Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy()
                ,camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;
    }


    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        MusicFactory.setAssetBasePath("mfx/");
        TextureManager.initTextures(this, getEngine());
    }

    @Override
    protected Scene onCreateScene() {
        mainMenu =  new MainMenuScene();
        return mainMenu;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainMenu != null){
            mainMenu.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mainMenu != null){
            mainMenu.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK ) && event.getAction() == KeyEvent.ACTION_DOWN){
            if (mainMenu != null){
                mainMenu.onKeyPressed(keyCode, event);
            }
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {

        getEngine().setScene(null);
        getEngine().clearUpdateHandlers();
        getEngine().clearDrawHandlers();
        GameContex.setGameActivity(null);
        GameDateHolder.setBonuses(null);
        SceneContext.setActiveScene(null);
        ObjectPoolContex.setBarrierCenter(null);
        mainMenu.destroy();
        mainMenu.detachChildren();
        mainMenu.detachSelf();
        mainMenu = null;
        TextureManager.clear();
        super.onDestroy();
        System.gc();
    }
}
