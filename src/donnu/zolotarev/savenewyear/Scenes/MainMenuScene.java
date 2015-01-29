package donnu.zolotarev.savenewyear.Scenes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.Date;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.AppUtils;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.FallingShow.ShowflakeGenerator;
import donnu.zolotarev.savenewyear.GameData.AchievementsHelper;
import donnu.zolotarev.savenewyear.GameData.Bonuses;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.GameData.Setting;
import donnu.zolotarev.savenewyear.MyObserver;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.DateEventProcessor;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;

public class MainMenuScene extends BaseScene implements MyObserver {

    private static final String PREF_NAME = "PREF_NAME";
    private static final String BONUS_COUNT = "BONUS_COUNT";
    private static final String PREF_SETTINGS = "PREF_SETTINGS";
    private static final String LAST_ENTER_TIME = "LAST_ENTER_TIME";
    private static final String ENTER_TIME_BONUS = "ENTER_TIME_BONUS";

    private static int adCount = 0;
    private ShowflakeGenerator generator;

    @Override
    public void update(int data) {
    }

    private enum LAYERS{
        TITLE_LAYER,
        SHOW_LAYER,
        BATTON_LAYER
    }

    private ISimpleClick onVkClick = new ISimpleClick() {
        @Override
        public void onClick() {
            AppUtils.open(GameContex.getCurrent(),Constants.VK_GROUP_URL);
        }
    };
    private ISimpleClick onTwitterClick = new ISimpleClick() {
        @Override
        public void onClick() {
            AppUtils.open(GameContex.getCurrent(),Constants.TWITTER_GROUP_URL);
        }
    };

    private ISimpleClick onGooglePlayClick = new ISimpleClick() {
        @Override
        public void onClick() {
            AppUtils.open(GameContex.getCurrent(), Constants.GOOGLE_PLAY_LINK);
        }
    };

    private ISimpleClick onClickLeaderboart = new ISimpleClick() {
        @Override
        public void onClick() {
            if (GameContex.getActionResolver().getSignedInGPGS()) {
                GameContex.getActionResolver().getLeaderboardGPGS();
            }else{
                GameContex.getActionResolver().loginGPGS();
                GameContex.getCurrent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseGameActivity context = GameContex.getCurrent();
                        Toast.makeText(context,R.string.leaderboard_unavaileble,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    };

    private ISimpleClick onClickAchievement = new ISimpleClick() {
        @Override
        public void onClick() {
            if (GameContex.getActionResolver().getSignedInGPGS()) {
                GameContex.getActionResolver().getAchievementsGPGS();
            }else{
                GameContex.getActionResolver().loginGPGS();
                GameContex.getCurrent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseGameActivity context = GameContex.getCurrent();
                        Toast.makeText(context,R.string.achievement_unavaileble,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    };

   // private GameScene activeScene;
    ISimpleClick onClickPlay = new ISimpleClick() {
        @Override
        public void onClick() {
            //activeScene = ;
            onClickRestart.onClick();
        }
    };

    public MainMenuScene() {
        super();
        TextureManager.loadMenuSprites();
        initBackground();
        loadData();
        loadGame();
        // todo Debugmode
        if (Constants.DEV_MODE) {
            GameDateHolder.getBonuses().setBonusCount(100);
        }
        chackDay();
        GameDateHolder.getBonuses().addObserver(this);
        update(GameDateHolder.getBonuses().getBonusCount());
    }

    private void chackDay() {
       final BaseGameActivity context = GameContex.getCurrent();
        SharedPreferences pref = GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        DateEventProcessor processor = new DateEventProcessor(pref.getLong(LAST_ENTER_TIME,0));
        if (!processor.isEnterToday()) {
        final int count;
        if (processor.isEnterYesToday()) {
            count = pref.getInt(ENTER_TIME_BONUS,0)+1;
        }else{
            count = 1;
        }

        pref.edit()
                .putInt(ENTER_TIME_BONUS,count)
                .putLong(LAST_ENTER_TIME,new Date().getTime())
                .commit();


            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String text = context.getString(R.string.get_every_day_bonus, count);
                    GameDateHolder.getBonuses().buy(-count);
                    new AlertDialog.Builder(GameContex.getCurrent())
                            .setMessage(text)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            });
        }
    }

    private void loadGame(){
        SharedPreferences pref = GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        GameDateHolder.getBonuses().setBonusCount(pref.getInt(BONUS_COUNT, 0));
        GameDateHolder.getSetting().setNeedTutorials(pref.getBoolean(PREF_SETTINGS, true));

    }

    private void saveGame() {
        GameContex.getCurrent().runOnUiThread(new Runnable() {
            public void run() {
                SharedPreferences pref = GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                pref.edit().putInt(BONUS_COUNT, GameDateHolder.getBonuses().getBonusCount());
                pref.edit().putBoolean(PREF_SETTINGS, GameDateHolder.getSetting().isNeedTutorials())
                .commit();
            }
        });
    }

    private void loadData() {
        GameDateHolder.intit(new Bonuses(),new AchievementsHelper(),new Setting());
    }


    @Override
    protected void initLayers() {
        for (LAYERS l:LAYERS.values()){
            attachChild(new Entity());
        }
    }

    private void initBackground() {
        BaseGameActivity main = GameContex.getCurrent();

        setBackground(new SpriteBackground(new Sprite(0, 0, TextureManager.getMenuBG(), main.getVertexBufferObjectManager())));
        RectangularShape title = EasyLayoutsFactory.alihment(createSprite(TextureManager.getGameTitle()), Constants.CAMERA_WIDTH / 2, 20, WALIGMENT.CENTER, HALIGMENT.TOP);
        attachToLayer(LAYERS.TITLE_LAYER,title);

        String text = main.getString(R.string.main_menu_play);

        RectangularShape btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickPlay), Constants.CAMERA_WIDTH / 2,
                Constants.CAMERA_HEIGHT - 190, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachToLayer(LAYERS.BATTON_LAYER, btn1);


        text = main.getString(R.string.main_menu_achievement);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickAchievement), Constants.CAMERA_WIDTH / 2 - 50,
                Constants.CAMERA_HEIGHT - 85, WALIGMENT.RIGHT, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachToLayer(LAYERS.BATTON_LAYER, btn1);

        text = main.getString(R.string.main_menu_leaderboards);
        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickLeaderboart), Constants.CAMERA_WIDTH / 2 + 50,
                Constants.CAMERA_HEIGHT - 85, WALIGMENT.LEFT, HALIGMENT.CENTER);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.BATTON_LAYER,btn2);

        if (Constants.SHOW_SNOW) {
            final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH_HALF+100,0,Constants.CAMERA_WIDTH+200,50);
            generator =  new ShowflakeGenerator(particleEmitter,10,20);
            generator.addParticleInitializer(new VelocityParticleInitializer( -40, -20,100, 200));
            generator.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
            generator.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f, 1.5f));
         //   generator.addParticleInitializer(new ExpireParticleInitializer(3f));

            attachToLayer(LAYERS.SHOW_LAYER,generator);
        }
        //   initShow();
        versionInfoUpdate();
        RectangularShape googlePlay = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getGooglePlayIcon()
                , main.getVertexBufferObjectManager(),null,null, onGooglePlayClick), Constants.CAMERA_WIDTH -10, 30, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(googlePlay);
        attachToLayer(LAYERS.BATTON_LAYER,googlePlay);

        float dist = 30;
        RectangularShape twitter = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getTwitterIcon()
                , main.getVertexBufferObjectManager(),null,null, onTwitterClick), Constants.CAMERA_WIDTH -10, googlePlay.getHeight()+googlePlay.getY()+ dist, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(twitter);
        attachToLayer(LAYERS.BATTON_LAYER,twitter);

        RectangularShape vk = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getVkIcon()
                , main.getVertexBufferObjectManager(),null,null, onVkClick), Constants.CAMERA_WIDTH -10, twitter.getHeight()+twitter.getY()+ dist, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(vk);
        attachToLayer(LAYERS.BATTON_LAYER,vk);
    }

    private void versionInfoUpdate() {
        PackageInfo packinfo = null;
        try {
            packinfo = GameContex.getCurrent().getPackageManager().getPackageInfo("donnu.zolotarev.savenewyear", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
        }
        Text text = new Text(5,Constants.CAMERA_HEIGHT-40, TextureManager.getBigFont(),"v" + packinfo.versionName.toString(),GameContex.getCurrent().getVertexBufferObjectManager());
        text.setScaleCenter(0,0);
        text.setScale(0.3f);
        attachToLayer(LAYERS.TITLE_LAYER, text);
    }


    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        if (getChildScene() != null) {
            ((IActivityCallback)getChildScene()).onKeyPressed(keyCode, event);

        }else{
            GameContex.getCurrent().finish();
        }
    }

    @Override
    public void destroy() {
        GameDateHolder.getBonuses().removeObserver(this);
        generator.clear();
        detachSelf();
        detachChildren();
        clearTouchAreas();
        clearEntityModifiers();
        clearUpdateHandlers();
        clearChildScene();
        onClickPlay = null;
        onClickRestart = null;
        onClickLeaderboart = null;
        onGooglePlayClick = null;
        onClickAchievement = null;
        onVkClick = null;
        onTwitterClick = null;
        generator = null;
    }

    @Override
    public void onPause() {
        saveGame();
        if (getChildScene() != null) {
            ((IActivityCallback)getChildScene()).onPause();
        }
    }



    private ISimpleClick onClickRestart =  new ISimpleClick() {
        @Override
        public void onClick() {
            System.gc();
            if (getChildScene() != null) {
                saveGame();
                ((IActivityCallback)getChildScene()).destroy();
            }


            clearChildScene();
            if (GameDateHolder.getSetting().isNeedTutorials()) {
                setChildScene(new HelpScreen(onClickRestart), false, true, true);
            }else {
                if (Constants.NEED_ADS) {
                    if (adCount == Constants.ADS_SHOW_DELAY) {
                        adCount = 0;
                        AdBuddiz.showAd(GameContex.getCurrent());
                    }else{
                        adCount++;
                    }
                }
                 setChildScene(new GameScene(onClickRestart), false, true, true);
            }
        }
    };

}
