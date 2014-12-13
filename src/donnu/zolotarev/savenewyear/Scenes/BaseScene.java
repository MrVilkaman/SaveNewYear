package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public abstract class BaseScene extends Scene implements IActivityCallback {


    protected final Main main;

    public BaseScene(Main main) {
        this.main = main;
        setBackgroundEnabled(true);
        initLayers();
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    protected abstract void initLayers();

    protected Sprite createSprite(ITiledTextureRegion textureRegion) {
        return new Sprite(0,0, textureRegion,main.getVertexBufferObjectManager());
    }
}
