package donnu.zolotarev.savenewyear.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.Scenes.BaseScene;
import donnu.zolotarev.savenewyear.Scenes.MainMenuScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import donnu.zolotarev.savenewyear.billing.util.IabHelper;
import donnu.zolotarev.savenewyear.billing.util.IabResult;
import donnu.zolotarev.savenewyear.billing.util.Inventory;
import donnu.zolotarev.savenewyear.billing.util.Purchase;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class Main extends SimpleBaseGameActivity {

    static final int RC_REQUEST = 10001;
    private static final String TAG = "BILLING";
    private static final String PAYLOAD = "12321312321";
    private Camera camera;
        private BaseScene mainMenu;
    private IabHelper mHelper;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        GameContex.setGameActivity(this);
        initBilling();
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
        mHelper = null;
        TextureManager.clear();
        super.onDestroy();
        System.gc();
    }


    ///
    private void initBilling(){
        mHelper = new IabHelper(this, Constants.BASE64_PUBLIC_KEY);

        // включаем дебагинг (в релизной версии ОБЯЗАТЕЛЬНО выставьте в false)
        mHelper.enableDebugLogging(true);

        // инициализируем; запрос асинхронен
        // будет вызван, когда инициализация завершится
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    return;
                }

                // чекаем уже купленное
                mHelper. queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                        Log.d(TAG, "Query inventory finished.");
                        if (result.isFailure()) {
                            Log.d(TAG, "Failed to query inventory: " + result);
                            return;
                        }


                    }
                });
            }
        });
    }

    public void buy(){
        try {
            mHelper.launchPurchaseFlow(this, Constants.BUY_ITEM_ID, RC_REQUEST,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            if (result.isFailure()) {
                                return;
                            }

                            if (!verifyDeveloperPayload(info)) {
                                return;
                            }
                            if (info.getSku().equals(Constants.BUY_ITEM_ID)) {
                                Toast.makeText(getApplicationContext(), "Purchase for disabling ads done.", Toast.LENGTH_SHORT);
                                // сохраняем в настройках, что отключили рекламу
                               GameDateHolder.getBonuses().addFromPurchase();
                            }
                        }
                    }, PAYLOAD);
        } catch (Exception e) {

        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        /*
         * TODO: здесь необходимо свою верификацию реализовать
         * Хорошо бы ещё с использованием собственного стороннего сервера.
         */

        return PAYLOAD.equals(payload);
    }
}
