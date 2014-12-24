package donnu.zolotarev.savenewyear.Scenes;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.widget.Toast;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.AppUtils;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.FallingShow.ShowflakeGenerator;
import donnu.zolotarev.savenewyear.GameData.Bonuses;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
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

public class MainMenuScene extends BaseScene {

    private static int adCount = 0;
    private ShowflakeGenerator generator;


    private enum LAYERS{
        TITLE_LAYER,
        SHOW_LAYER,
        BATTON_LAYER
    }


    private ISimpleClick onClickShop = new ISimpleClick() {
        @Override
        public void onClick() {
            GameContex.getCurrent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseGameActivity context = GameContex.getCurrent();
                    Toast.makeText(context,R.string.unavaileble,Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private ISimpleClick onGooglePlayClick = new ISimpleClick() {
        @Override
        public void onClick() {
//            ((Main)GameContex.getCurrent()).buy();
            AppUtils.rateMe(GameContex.getCurrent());
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
    }

    private void loadData() {
        GameDateHolder.setBonuses(new Bonuses());
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
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickPlay), Constants.CAMERA_WIDTH / 2 - 50,
                Constants.CAMERA_HEIGHT - 190, WALIGMENT.RIGHT, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachToLayer(LAYERS.BATTON_LAYER, btn1);

        text = main.getString(R.string.main_menu_leaderboards);
        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                , main.getVertexBufferObjectManager(),text,TextureManager.getFont(), onClickLeaderboart), Constants.CAMERA_WIDTH / 2 + 50,
                Constants.CAMERA_HEIGHT-190, WALIGMENT.LEFT, HALIGMENT.CENTER);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.BATTON_LAYER,btn2);

        text = main.getString(R.string.main_menu_achievement);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickAchievement), Constants.CAMERA_WIDTH / 2 - 50,
                Constants.CAMERA_HEIGHT - 85, WALIGMENT.RIGHT, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachToLayer(LAYERS.BATTON_LAYER, btn1);

        text = main.getString(R.string.main_menu_shop);
        btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(),text,TextureManager.getFont(), onClickShop), Constants.CAMERA_WIDTH / 2 + 50,
                Constants.CAMERA_HEIGHT-85, WALIGMENT.LEFT, HALIGMENT.CENTER);
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
        RectangularShape gpbtn = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getGooglePlay()
                , main.getVertexBufferObjectManager(),null,null, onGooglePlayClick), Constants.CAMERA_WIDTH +5, -5, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(gpbtn);
        gpbtn.setScale(0.5f);
        attachToLayer(LAYERS.BATTON_LAYER,gpbtn);
    }

    private void versionInfoUpdate() {
        PackageInfo packinfo = null;
        try {
            packinfo = GameContex.getCurrent().getPackageManager().getPackageInfo("donnu.zolotarev.savenewyear", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
        }
        Text text = new Text(5,5, TextureManager.getBigFont(),"v" + packinfo.versionName.toString(),GameContex.getCurrent().getVertexBufferObjectManager());
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
        onClickShop = null;
        generator = null;
    }

    @Override
    public void onPause() {
        if (getChildScene() != null) {
            ((IActivityCallback)getChildScene()).onPause();
        }
    }

    private ISimpleClick onClickRestart =  new ISimpleClick() {
        @Override
        public void onClick() {
            System.gc();
            if (getChildScene() != null) {
                ((IActivityCallback)getChildScene()).destroy();
            }
            if (Constants.NEED_ADS) {
                if (adCount == Constants.ADS_SHOW_DELAY) {
                    adCount = 0;
                    AdBuddiz.showAd(GameContex.getCurrent());
                }else{
                    adCount++;
                }
            }

            clearChildScene();
            setChildScene(new GameScene(onClickRestart), false, true, true);
        }
    };

}
