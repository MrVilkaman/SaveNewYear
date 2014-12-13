package donnu.zolotarev.savenewyear.Activities;

import donnu.zolotarev.savenewyear.Constants;
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
        return new MainMenuScene(this);
    }


    @Override
    public void onDestroyResources() throws Exception {
        TextureManager.unloadGameSprites();
        TextureManager.clear();
        super.onDestroyResources();
    }
}
