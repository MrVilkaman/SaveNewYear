package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import org.andengine.entity.scene.Scene;

public class BaseScene extends Scene implements IActivityCallback {


    protected final Main main;

    public BaseScene(Main main) {
        this.main = main;
        setBackgroundEnabled(true);
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
