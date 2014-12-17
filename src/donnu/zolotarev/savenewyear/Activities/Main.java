package donnu.zolotarev.savenewyear.Activities;

import android.os.Bundle;
import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameContex;
import donnu.zolotarev.savenewyear.Scenes.BaseScene;
import donnu.zolotarev.savenewyear.Scenes.MainMenuScene;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
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
        mainMenu =  new MainMenuScene(this);
        return mainMenu;
    }


    @Override
    public void onDestroyResources() throws Exception {
      /*  TextureManager.unloadGameSprites();
        TextureManager.clear();*/
        super.onDestroyResources();
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
        /*try {
            MusicLoader.getSound().pause();
        } catch (MusicReleasedException e) {
        }*/
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
        super.onDestroy();
        GameContex.setGameActivity(null);
    }
}
