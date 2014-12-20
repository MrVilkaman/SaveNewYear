package donnu.zolotarev.savenewyear.Scenes;

import android.content.Context;
import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.BarrierWave.ICanUnitCreate;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.WaveController;
import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.BaseUnit;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Barriers.Menegment.BarrierCenter;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.FallingShow.ShowflakeGenerator;
import donnu.zolotarev.savenewyear.Hero;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.ObjectCollisionController;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import donnu.zolotarev.savenewyear.Utils.ParallaxLayer;
import donnu.zolotarev.savenewyear.Utils.Utils;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
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

public class GameScene extends BaseScene implements IActiveGameScene,ICanUnitCreate {


    private static final float PARALLAX_CHANGE_PER_SECOND = 10;
    private  static final int UPDATE_TIMER_COUNTER_MAX = 6;

    private static final float BACKGROUND_LAYER_SPEED = 0.6f;
    private static final float FRONT_LAYER_COEF = 1.3f;
    private static final float GAME_LAYER_COEF = 1f;
    private static final int GROUND_Y = 561;
    private static final float SPEED_COEF = 1.1f;

    private static final String MAX_TIME = "MAX_TIME";
    private static final String PREF_NAME = "PREF_NAME";
    private ObjectCollisionController<ICollisionObject> treeCollection;
    private boolean flag2 = true;

    private ParallaxLayer parallaxFG;
    private ParallaxLayer parallaxRoad;
    private AutoParallaxBackground autoParallaxBackground;

    private float gameSpeed = 500;
    private float gameGroundY = GROUND_Y;
    private BarrierKind lastItemType;


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

    private Date bestTime = new Date(0);

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

        ObjectPoolContex.setBarrierCenter(new BarrierCenter());
        waveController = new WaveController(this);
        waveController.start();

        loadGame();

        final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH_HALF+500,0,Constants.CAMERA_WIDTH+300,100);
        ShowflakeGenerator generator =  new ShowflakeGenerator(particleEmitter,20,30);
        generator.addParticleInitializer(new VelocityParticleInitializer( -200, -400,100, 200));
        generator.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
        generator.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f, 1.5f));
        generator.addParticleInitializer(new ExpireParticleInitializer(5f));

        attachToLayer(LAYERS.FRONT_LAYER,generator);


    }

    private void onGameOver() {
              showHud(false);
        enablePauseMenu = false;
        isShowMenuScene = true;
        if (pauseMenu == null) {
            pauseMenu = new PauseMenuScene(onClickResume, onClickRestart, onClickExit);
        }
        pauseMenu.setTime(date, bestTime, true);
        setChildScene(pauseMenu, false, true, true);
        if (bestTime.getTime() < date.getTime()) {
            bestTime = date;
        }
        saveGame();
    }

    @Override
    public float getGameSpeed() {
        return gameSpeed;
    }

    @Override
    public void registerTouchArea(ITouchArea entity) {
         super.registerTouchArea(entity);
    }

    @Override
    public float getGroundY() {
        return gameGroundY;
    }

    @Override
    public void setGroudY(float i) {
        gameGroundY = i;
    }

    @Override
    public void updateGameSpeed(){
        gameSpeed *=SPEED_COEF;
        parallaxRoad.setParallaxChangePerSecond(gameSpeed);
        parallaxFG.setParallaxChangePerSecond(gameSpeed);
        autoParallaxBackground.setParallaxChangePerSecond(gameSpeed);
        ArrayList<ICollisionObject> col = treeCollection.get();
        for (int i = col.size()-1; 0<=i;i--){
            BaseUnit it =  (BaseUnit)col.get(i);
            it.updateSpeed();
        }
    }

    @Override
    public void initNextUnit() {
        IBarrier item;
        double r;
        BarrierKind itemType;

     /*   do {
            r = Math.random();
            if(r<0.25) {
                itemType = BarrierKind.NEW_YEAR_TREE;
            }else if (r<0.5){
                itemType = BarrierKind.WATER_HOLL;
            }else if (r<0.75){
                itemType = BarrierKind.SHOW_BALL;
            }else{
                itemType = BarrierKind.TREE;
            }
        } while (itemType == lastItemType);
        lastItemType = itemType;*/

        item = ObjectPoolContex.getBarrierCenter().getUnit( BarrierKind.TREE);
        waveController.addOvertime(item.getOverTime());
        item.setStart();
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
         autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,gameSpeed);
        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
       autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-BACKGROUND_LAYER_SPEED, background));
        setBackground(autoParallaxBackground);

        parallaxFG = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxFG.setParallaxChangePerSecond(gameSpeed);
        parallaxFG.setParallaxScrollFactor(1);
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        parallaxFG.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-FRONT_LAYER_COEF, frontground, false, 1));
        attachToLayer(LAYERS.FRONT_LAYER, parallaxFG);

        parallaxRoad = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxRoad.setParallaxChangePerSecond(gameSpeed);
        parallaxRoad.setParallaxScrollFactor(1);
        Sprite sprite = new Sprite(0,Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight()-15,TextureManager.getRoad(),main.getVertexBufferObjectManager());
        parallaxRoad.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_COEF, sprite));
        attachToLayer(LAYERS.ROAD_LAYER, parallaxRoad);

    }

    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }

    @Override
    public void attachToGameLayers(IEntity entity, boolean beforHero) {
        if (beforHero) {
            entity.setZIndex(hero.getSprite().getZIndex()-1);
        }
            attachToLayer(LAYERS.GAME_LAYER,entity);
        if (beforHero) {
            getChildByIndex(LAYERS.GAME_LAYER.ordinal()).sortChildren();
        }
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

    private void saveGame(){
       GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
               .putLong(MAX_TIME, bestTime.getTime())
               .commit();
    }

    private void loadGame(){
        bestTime = new Date(GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getLong(MAX_TIME,0));

    }
}
