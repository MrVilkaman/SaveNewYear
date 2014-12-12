package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;

public class BaseScene extends Scene implements IActivityCallback {


    public BaseScene(Main main) {
        setBackgroundEnabled(true);
        AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,15);

        TextureManager.loadGameSprites();

        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-4.0f, background));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, frontground));
        setBackground(autoParallaxBackground);
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
}
