package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public abstract class BaseScene extends Scene implements IActivityCallback {

    public BaseScene() {
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
        return new Sprite(0,0, textureRegion, GameContex.getCurrent().getVertexBufferObjectManager());
    }
}
