package donnu.zolotarev.savenewyear.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.fullscreen.RevMobFullscreen;
import com.revmob.ads.link.RevMobLink;

import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Scenes.BaseScene;
import donnu.zolotarev.savenewyear.Scenes.MainMenuScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.AppRater;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import donnu.zolotarev.savenewyear.billing.util.IabHelper;
import donnu.zolotarev.savenewyear.billing.util.IabResult;
import donnu.zolotarev.savenewyear.billing.util.Inventory;
import donnu.zolotarev.savenewyear.billing.util.Purchase;
import playservice.basegameutils.GameHelper;

public class Main extends SimpleBaseGameActivity implements ActionResolver,IAnalistyc{
    private static final int RC_REQUEST = 10001;
    private static final String TAG = "BILLING";
    private static final String PAYLOAD = "12321312321";
    private BaseScene mainMenu;
    private IabHelper mHelper;
    private GameHelper gameHelper;

    private RevMobFullscreen revFull;
    private RevMobLink revLink;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        GameContex.setGameActivity(this);
        initBilling();
        initPlayService();

        if (Constants.NEED_ANALYTICS) {
            GoogleAnalytics.getInstance(this).enableAutoActivityReports(getApplication());

            try {
                GoogleAnalytics.getInstance(this).dispatchLocalHits();
            } catch (Exception e) {
            }
        }

        try {
            AppRater.app_launched(this);
        } catch (Exception e) {
        }

      //  loadBigBanner();
        if (Constants.NEED_ADS) {
            RevMob revmob = RevMob.start(this); // RevMob Media ID configured in the AndroidManifest.xml file
            revFull = revmob.createFullscreen(this,list);
            revLink = revmob.createAdLink(this, list);
        }
    }

    void loadBigBanner(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final InterstitialAd interstitial = new InterstitialAd(Main.this);
                interstitial.setAdUnitId(Constants.ADMOB_DS_ID);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("0941BB9226FB875E4646CDF71CF1D248")
                        .build();

                interstitial.loadAd(adRequest);
                interstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        sendReport("ADS","ADMOD error",errorCode+"");
                        if (revFull != null) {
                            revFull.show();
                        }
                    }


                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        sendReport("ADS","ADMOD Load","");
                        if (interstitial.isLoaded()){
                            interstitial.show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy()
                , camera);
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
        if (mainMenu != null) {
            mainMenu.destroy();
            mainMenu.detachChildren();
            mainMenu.detachSelf();
            mainMenu = null;
        }
        if (revLink != null) {
            revLink.cancel();
            revLink = null;
        }
        if (revFull != null) {
            revFull = null;
        }
        getEngine().setScene(null);
        getEngine().clearUpdateHandlers();
        getEngine().clearDrawHandlers();
        GameContex.setGameActivity(null);
        GameDateHolder.clear();
        SceneContext.setActiveScene(null);
        ObjectPoolContex.setBarrierCenter(null);

        mHelper = null;
        TextureManager.clear();
        super.onDestroy();
        System.gc();
    }

    private void initPlayService(){
        if (Constants.NEED_PLAY_SERVICE) {
            gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            gameHelper.setConnectOnStart(true);
            gameHelper.enableDebugLog(false);
            gameHelper.setup(new GameHelper.GameHelperListener() {
                @Override
                public void onSignInFailed() {
            //        Toast.makeText(Main.this, "Fail =( ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSignInSucceeded() {
                    incrementAchievementGPGS(R.string.achievement_purposeful, 1);
                //    Toast.makeText(Main.this, "It is work!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public boolean getSignedInGPGS() {
        return Constants.NEED_PLAY_SERVICE && gameHelper.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // инициировать вход пользователя. Может быть вызван диалог
                    // входа. Выполняется в UI-потоке
                    try {
                        gameHelper.beginUserInitiatedSignIn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submitScoreGPGS(long score) {

        if (getSignedInGPGS()) {
        GameDateHolder.getAchievementsHelper().proccessBestTime(score);
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_normal_mode), score);
        }
    }

    @Override
    public void getLeaderboardGPGS() {
        if (getSignedInGPGS()) {
            startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(gameHelper
                            .getApiClient(), getString(R.string.leaderboard_normal_mode)), 100);
        }
    }

    @Override
    public void unlockAchievementGPGS(int achievementId) {
        // открыть достижение с ID achievementId
        if (getSignedInGPGS()) {
            Games.Achievements.unlock(gameHelper.getApiClient(), getString(achievementId));
        }
    }

    @Override
    public void incrementAchievementGPGS(int achievementId, int val) {
        // открыть достижение с ID achievementId
        if (getSignedInGPGS()) {
            Games.Achievements.increment(gameHelper.getApiClient(), getString(achievementId), val);
        }
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public void getAchievementsGPGS() {
        if (getSignedInGPGS()) {
            startActivityForResult(
                    Games.Achievements.getAchievementsIntent(gameHelper
                            .getApiClient()), 101);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Constants.NEED_PLAY_SERVICE) {
            gameHelper.onStart(this);
        }

        if (Constants.NEED_ANALYTICS){
            sendReport("Load game");
            GoogleAnalytics.getInstance(this).reportActivityStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Constants.NEED_PLAY_SERVICE) {
            gameHelper.onStop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.NEED_PLAY_SERVICE) {
            gameHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void sendReport(String message) {
        if (Constants.NEED_ANALYTICS) {
            try {
                Tracker tracker = GoogleAnalytics.getInstance(Main.this).newTracker(Constants.ANALISTYC_TRACER_ID);
                tracker.setScreenName(message);

                tracker.send(new HitBuilders.AppViewBuilder().build());
            } catch (Exception e) {
            }
        }
    }

    public void sendReport(String category,String action, String label){
        if (Constants.NEED_ANALYTICS) {
            try {
                Tracker tracker = GoogleAnalytics.getInstance(Main.this).newTracker(Constants.ANALISTYC_TRACER_ID);

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .build());
            } catch (Exception e) {
            }
        }
    }
    public void sendReport(String category,String action, String label,long value){
        if (Constants.NEED_ANALYTICS) {
            try {
                Tracker tracker = GoogleAnalytics.getInstance(Main.this).newTracker(Constants.ANALISTYC_TRACER_ID);

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .setValue(value)
                        .build());
            } catch (Exception e) {
            }
        }
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
//                                Toast.makeText(getApplicationContext(), "Purchase for disabling ads done.", Toast.LENGTH_SHORT);
                                // сохраняем в настройках, что отключили рекламу
                               GameDateHolder.getBonuses().addFromPurchase();
                                GameDateHolder.getAchievementsHelper().proccessFromPurchase();
                               sendReport("IN_APP","Buy 50 bonus!","");
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

    @Override
    public void showAds() {
        if (Constants.NEED_ADS) {
            loadBigBanner();
        }
    }

    @Override
    public void openAdLink() {
        if (revLink != null) {
            revLink.open();
        }
    }

    private RevMobAdsListener list = new RevMobAdsListener() {
        @Override
        public void onRevMobSessionIsStarted() {

        }

        @Override
        public void onRevMobSessionNotStarted(String s) {
            sendReport("ADS","Revmob error",s);
        }

        @Override
        public void onRevMobAdReceived() {

        }

        @Override
        public void onRevMobAdNotReceived(String s) {
            sendReport("ADS","Revmob error",s);
        }

        @Override
        public void onRevMobAdDisplayed() {
            sendReport("ADS","Revmob Displayed","");
        }

        @Override
        public void onRevMobAdDismiss() {

        }

        @Override
        public void onRevMobAdClicked() {
            sendReport("ADS","Revmob Clicked","");
        }

        @Override
        public void onRevMobEulaIsShown() {

        }

        @Override
        public void onRevMobEulaWasAcceptedAndDismissed() {

        }

        @Override
        public void onRevMobEulaWasRejected() {

        }
    };
}
