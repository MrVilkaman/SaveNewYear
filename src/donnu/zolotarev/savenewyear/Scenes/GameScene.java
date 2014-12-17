package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.BarrierWave.ICanUnitCreate;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.WaveController;
import donnu.zolotarev.savenewyear.Barriers.BarrierCenter;
import donnu.zolotarev.savenewyear.*;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.ObjectCollisionController;
import donnu.zolotarev.savenewyear.Utils.ParallaxLayer;
import donnu.zolotarev.savenewyear.Utils.Utils;
import org.andengine.engine.handler.IUpdateHandler;
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
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;
import java.util.Date;

public class GameScene extends BaseScene implements IHaveGameLayers,ICanUnitCreate {


    private static final float PARALLAX_CHANGE_PER_SECOND = 10;
    private  static final int UPDATE_TIMER_COUNTER_MAX = 6;

    private static final int FRONT_LAYER_SPEED = 55;
    private static final int BACKGROUND_LAYER_SPEED = 15;
    private static final int GAME_LAYER_SPEED = 50;

    private static final int GROUND_Y = 500;
    private ObjectCollisionController<ICollisionObject> treeCollection;
    private boolean flag2 = true;


    private enum LAYERS{
        ROAD_LAYER,
        GAME_LAYER,
        FRONT_LAYER,
        HUD_LAYER
    }

    private final ISimpleClick onClickRestart;
    private boolean enablePauseMenu = true;
    private boolean isShowMenuScene = false;

    private PauseMenuScene pauseMenu;
    private Text timerScore;
    private float gameTime = 0;
    private Date bestTime = new Date(55501);

    private ISimpleClick onClickPause = new ISimpleClick() {
        @Override
        public void onClick() {
            showPause();
        }
    };
    private int updateTimerCounter = 0;
    private Hero hero;

    private Date date = new Date(0);

    private IWaveController waveController;


    public GameScene(ISimpleClick onClickRestart) {
        super();
        this. onClickRestart = onClickRestart;
        TextureManager.loadGameSprites();
        SceneContext.setActiveScene(this);
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

        registerUpdateHandler(new IUpdateHandler(){

            @Override
            public void onUpdate(float pSecondsElapsed) {
                ArrayList<ICollisionObject> objects = treeCollection.haveCollision(hero);
//                for (int i = objects.size()-1; 0<=i;i--){
                if (objects.size() != 0) {
                    if (flag2) {
                        flag2 = false;
                        onGameOver();
                    }
                }else {
                    flag2 = true;
                }
//                }
            }

            @Override
            public void reset() {

            }
        });

        ObjectPoolContex.setBarrierCenter(new BarrierCenter());
        waveController = new WaveController(this);
        waveController.start();
    }

    private void onGameOver() {
        /*onClickExit.onClick();
        GameContex.getCurrent().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(GameContex.getCurrent(), "Лузер! И твое время " + sdf.format(date), Toast.LENGTH_SHORT).show();
            }
        });*/

        showHud(false);
        enablePauseMenu = false;
        if (pauseMenu == null) {
            pauseMenu = new PauseMenuScene(onClickResume, onClickRestart, onClickExit);
        }
        pauseMenu.setTime(date,bestTime, true);
        setChildScene(pauseMenu, false, true, true);
    }

    @Override
    public void initNextUnit() {
        TreeItem item = ObjectPoolContex.getBarrierCenter().getUnit();
        item.setStart(561);
    }

    private void createHUD() {
        BaseGameActivity main = GameContex.getCurrent();
        timerScore = new Text(0,0, TextureManager.getBigFont(),"01:23.456",main.getVertexBufferObjectManager());
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,-10, WALIGMENT.CENTER, HALIGMENT.TOP);

        attachToLayer(LAYERS.HUD_LAYER, timerScore);

        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getPauseButton()
                , main.getVertexBufferObjectManager(),null,null, onClickPause),Constants.CAMERA_WIDTH-20, 20, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.HUD_LAYER, btn2);
    }

    private void initOthers() {
        treeCollection =  new ObjectCollisionController<ICollisionObject>();
        hero = new Hero();
    }

    @Override
    protected void initLayers() {
        for (LAYERS l:LAYERS.values()){
            attachChild(new Entity());
        }
    }

    private void createBackGround() {
        BaseGameActivity main = GameContex.getCurrent();
        AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,PARALLAX_CHANGE_PER_SECOND);
        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
       autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-BACKGROUND_LAYER_SPEED, background));

        setBackground(autoParallaxBackground);

        ParallaxLayer parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND);
        parallaxLayer.setParallaxScrollFactor(1);
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-FRONT_LAYER_SPEED, frontground,false,1));
        attachToLayer(LAYERS.FRONT_LAYER,parallaxLayer);

        parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND);
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
    public void attachSelfToCollection(ICollisionObject collisionObject) {
        treeCollection.add(collisionObject);
    }

    @Override
    public void detachSelfFromCollection(ICollisionObject collisionObject) {
        treeCollection.remove(collisionObject);
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        if (enablePauseMenu){
            if (!isShowMenuScene){
                showPause();
            }else{
                isShowMenuScene = false;
                clearChildScene();
                showHud(true);
                //back();
            }
        }
    }

    private void showPause() {
        showHud(false);
        isShowMenuScene = true;
        if (pauseMenu == null) {
            pauseMenu = new PauseMenuScene(onClickResume, onClickRestart, onClickExit);
        }
        pauseMenu.setTime(date,bestTime, false);
        setChildScene(pauseMenu, false, true, true);
    }

    private final ISimpleClick onClickResume = new ISimpleClick() {
        @Override
        public void onClick() {
            clearChildScene();
            isShowMenuScene = false;
            showHud(true);
        }
    };
    private ISimpleClick onClickExit =  new ISimpleClick() {
        @Override
        public void onClick() {
            back();
        }
    };



    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (!isShowMenuScene) {
            gameTime += pSecondsElapsed;
            // todo оптимизировать по памяти
            if (updateTimerCounter < 0) {
                date.setTime((int)(gameTime*1000));
                timerScore.setText(Utils.timerFormat(date));
                updateTimerCounter = UPDATE_TIMER_COUNTER_MAX;
            }
            updateTimerCounter--;
            waveController.update(pSecondsElapsed);

        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    private void showHud(boolean flag){
        getChildByIndex(LAYERS.HUD_LAYER.ordinal()).setVisible(flag);
    }


    @Override
    public void onDetached() {
        SceneContext.setActiveScene(null);
        ObjectPoolContex.setBarrierCenter(null);
    }
}
