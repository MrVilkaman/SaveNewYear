package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Hero;
import donnu.zolotarev.savenewyear.IHaveGameLayers;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.ParallaxLayer;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameScene extends BaseScene implements IHaveGameLayers {



    private enum LAYERS{
        ROAD_LAYER,
        GAME_LAYER,
        FRONT_LAYER,
        HUD_LAYER
    }

    private  static final int UPDATE_TIMER_COUNTER_MAX = 6;

    private static final int FRONT_LAYER_SPEED = 40;
    private static final int BACKGROUND_LAYER_SPEED = 15;
    private static final int GAME_LAYER_SPEED = 30;

    private final ISimpleClick onClickRestart;
    private boolean enablePauseMenu = true;
    private boolean isShowMenuScene = false;

    private PauseMenuScene pauseMenu;
    private Text timerScore;
    private float gameTime = 0;


    private ISimpleClick onClickPause = new ISimpleClick() {
        @Override
        public void onClick() {
            showPause();
        }
    };
    private int updateTimerCounter = 0;
    private Hero hero;


    public GameScene(Main main, ISimpleClick onClickRestart) {
        super(main);
        this. onClickRestart = onClickRestart;
        TextureManager.loadGameSprites();
        createBackGround();
        createHUD();
        initOthers();

        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    hero.jump();
                    return true;
                }
                return false;
            }
        });
    }

    private void createHUD() {
        timerScore = new Text(0,0, TextureManager.getBigFont(),"01:23.456",main.getVertexBufferObjectManager());
        //timerScore.setScale(2f);
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,-10, WALIGMENT.CENTER, HALIGMENT.TOP);

        attachToLayer(LAYERS.HUD_LAYER, timerScore);

        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getPauseButton()
                , main.getVertexBufferObjectManager(),null,null, onClickPause),Constants.CAMERA_WIDTH-20, 20, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.HUD_LAYER, btn2);
    }

    private void initOthers() {
        hero = new Hero(main,this);
    }

    @Override
    protected void initLayers() {
        for (LAYERS l:LAYERS.values()){
            attachChild(new Entity());
        }
    }

    private void createBackGround() {
        AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,10);
        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
       autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-BACKGROUND_LAYER_SPEED, background));

        setBackground(autoParallaxBackground);

        ParallaxLayer parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(8);
        parallaxLayer.setParallaxScrollFactor(1);
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-FRONT_LAYER_SPEED, frontground,false,1));
        attachToLayer(LAYERS.FRONT_LAYER,parallaxLayer);

        parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(10);
        parallaxLayer.setParallaxScrollFactor(1);
        Sprite sprite = new Sprite(0,Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight()-15,TextureManager.getRoad(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_SPEED, sprite));
        attachToLayer(LAYERS.ROAD_LAYER,parallaxLayer);
    }

    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }

    @Override
    public void attachToGameLayers(IEntity entity) {
        attachToLayer(LAYERS.GAME_LAYER,entity);
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        if (enablePauseMenu){
            if (!isShowMenuScene){
                showPause();
            }else{
                isShowMenuScene = false;
                clearChildScene();
                //back();
            }
        }
    }

    private void showPause() {
        isShowMenuScene = true;
        if (pauseMenu == null) {
            pauseMenu = new PauseMenuScene(main, onClickResume, onClickRestart, onClickExit);
        }
        setChildScene(pauseMenu, false, true, true);
    }

    private final ISimpleClick onClickResume = new ISimpleClick() {
        @Override
        public void onClick() {
            clearChildScene();
            isShowMenuScene = false;
        }
    };
    private ISimpleClick onClickExit =  new ISimpleClick() {
        @Override
        public void onClick() {
            back();
        }
    };


    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.S", Locale.ENGLISH);
    Date date = new Date(0);
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (!isShowMenuScene) {
            gameTime += pSecondsElapsed;
            // todo оптимизировать по памяти
            if (updateTimerCounter < 0) {
                date.setTime((int)(gameTime*1000));
                timerScore.setText(sdf.format(date));
                updateTimerCounter = UPDATE_TIMER_COUNTER_MAX;
            }
            updateTimerCounter--;
        }
        super.onManagedUpdate(pSecondsElapsed);
    }
}
