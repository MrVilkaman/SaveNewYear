package donnu.zolotarev.savenewyear.Scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.BarrierWave.ICanUnitCreate;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.WaveController;
import donnu.zolotarev.savenewyear.Barriers.BaseUnit;
import donnu.zolotarev.savenewyear.Barriers.Menegment.BarrierCenter;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.FallingShow.ShowflakeGenerator;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.Hero;
import donnu.zolotarev.savenewyear.MyObserver;
import donnu.zolotarev.savenewyear.R;
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
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Date;

public class GameScene extends BaseScene implements IActiveGameScene,ICanUnitCreate {


    public static final int GIFT_FOR_LIFE = 1;
    private static final float PARALLAX_CHANGE_PER_SECOND = 10;
    private  static final int UPDATE_TIMER_COUNTER_MAX = 6;

    private static final float BACKGROUND_LAYER_SPEED = 0.6f;
    private static final float FRONT_LAYER_COEF = 1.3f;
    private static final float GAME_LAYER_COEF = 1f;
    private static final int GROUND_Y = 561;
    private static final float SPEED_COEF = 1.085f;

    private static final float GIFT_TIME_MAX = 50f;
    private static final long DELAY_GAMEOVER = 1000; //mc

    private static final String MAX_TIME = "MAX_TIME";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String BONUS_COUNT = "BONUS_COUNT";

    private ObjectCollisionController<ICollisionObject> treeCollection;
    private boolean flag2 = true;

    private ParallaxLayer parallaxFG;
    private ParallaxLayer parallaxRoad;
    private AutoParallaxBackground autoParallaxBackground;

    private float gameSpeed = 550;
    private float gameGroundY = GROUND_Y;

    private ShowflakeGenerator generator;
    private Text presentScore;

    private float giftTime = 0;


    private enum LAYERS{
        ROAD_LAYER,
        GAME_LAYER,
        FRONT_LAYER,
        HUD_LAYER
    }

    private ISimpleClick onClickRestart;
    private boolean isNotGameOver = true;
    private boolean isShowMenuScene = false;
    private boolean enabledPauseMenu = true;

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
        this.onClickRestart = onClickRestart;
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
        updateGameSpeed();
        updatePresent(GameDateHolder.getBonuses().getBonusCount());

   //     createFPSBase();
    }

    private void createFPSBase() {
        final FPSCounter fpsCounter = new FPSCounter();
        Engine engine = GameContex.getCurrent().getEngine();
        engine.registerUpdateHandler(fpsCounter);
        final Text fpsText = new Text(0, 150    , TextureManager.getFont(), "FPS:", "FPS: 1234567890.".length(),engine.getVertexBufferObjectManager());
        fpsText.setScale(0.5f);
        fpsText.setColor(Color.WHITE);
        attachChild(fpsText);
        fpsText.setZIndex(1000);
        registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {

                fpsText.setText("FPS: " + String.valueOf(fpsCounter.getFPS()));
            }
        }));
    }

    private void onGameOver() {
              showHud(false);
        enabledPauseMenu = false;
        GameContex.getCurrent().runOnUiThread(new Runnable() {
            @Override
            public void run() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enabledPauseMenu = false;
                isNotGameOver = false;
                //isShowMenuScene = true;
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
        },DELAY_GAMEOVER);

            }
        });
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
    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed  = gameSpeed;
        updateGameSpeed();
    }

    @Override
    public void increaseGameSpeed(){
        gameSpeed *=SPEED_COEF;
        updateGameSpeed();
    }

    private void updateGameSpeed() {
        if (Constants.SHOW_SHOW) {
            generator.setSpeed(gameSpeed);
        }
        parallaxRoad.setParallaxChangePerSecond(gameSpeed);
        parallaxFG.setParallaxChangePerSecond(gameSpeed);
        autoParallaxBackground.setParallaxChangePerSecond(gameSpeed);
        ArrayList<ICollisionObject> col = treeCollection.get();
        for (int i = col.size()-1; 0<=i;i--){
            BaseUnit it =  (BaseUnit)col.get(i);
            it.updateSpeed();
        }
    }

    private void createHUD() {
        BaseGameActivity main = GameContex.getCurrent();
        // выделение памяти под цифры, чтобы не было  рывков!
        new Text(0,0, TextureManager.getFont(),"x1234567890",main.getVertexBufferObjectManager());
        new Text(0,0, TextureManager.getBigFont(),"8970",main.getVertexBufferObjectManager());

        timerScore = new Text(0,0, TextureManager.getBigFont(),"01:23.456",main.getVertexBufferObjectManager());
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,-10, WALIGMENT.CENTER, HALIGMENT.TOP);

        attachToLayer(LAYERS.HUD_LAYER, timerScore);

        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getPauseButton()
                , main.getVertexBufferObjectManager(),null,null, onClickPause),Constants.CAMERA_WIDTH-20, 20, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.HUD_LAYER, btn2);

        RectangularShape present = EasyLayoutsFactory.alihment(createSprite(TextureManager.getPresent()),5,8,WALIGMENT.LEFT, HALIGMENT.TOP);

        presentScore = new Text(0,0, TextureManager.getFont(),"x 000",main.getVertexBufferObjectManager());
        presentScore = (Text)EasyLayoutsFactory.alihment( presentScore
                ,present.getX()+present.getWidth()+5, present.getY() +present.getHeight()+10, WALIGMENT.LEFT, HALIGMENT.BOTTOM);
        attachToLayer(LAYERS.HUD_LAYER, present);
        attachToLayer(LAYERS.HUD_LAYER, presentScore);
    }

    private void initOthers() {
        treeCollection =  new ObjectCollisionController<ICollisionObject>();
        hero = new Hero();

        GameDateHolder.getBonuses().addObserver(new MyObserver() {
            @Override
            public void update(int data) {
                updatePresent(data);
            }
        });
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

        if (Constants.SHOW_SHOW) {
            final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH*3/2.f,0,Constants.CAMERA_WIDTH*3,300);
            generator =  new ShowflakeGenerator(particleEmitter,30,45);
            generator.addParticleInitializer(new VelocityParticleInitializer( -200, 200,350, 500));
            generator.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
            generator.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f, 1.5f));
//            generator.addParticleInitializer(new ExpireParticleInitializer(3f));
            // parallaxFG.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_COEF,generator));
            attachToLayer(LAYERS.FRONT_LAYER, generator);
        }


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
        if (enabledPauseMenu){
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

    @Override
    public void destroy() {
        treeCollection.cleer();
        generator.clear();
        generator = null;
        hero = null;
        SceneContext.setActiveScene(null);
        ObjectPoolContex.getBarrierCenter().clear();
        ObjectPoolContex.setBarrierCenter(null);
        GameDateHolder.getBonuses().deleteObservers();
        onClickRestart = null;
        onClickExit = null;
        onClickPause = null;
        onClickResume = null;
        detachChildren();
        detachSelf();
        clearTouchAreas();
        clearEntityModifiers();
        clearUpdateHandlers();
        clearChildScene();
        if (pauseMenu != null) {
            pauseMenu.destroy();
            pauseMenu = null;
        }
        parallaxFG = null;
        parallaxRoad = null;
        autoParallaxBackground = null;
        pauseMenu = null;
        timerScore = null;
        waveController = null;
        treeCollection = null;

    }

    private void showPause() {
        saveGame();
        showHud(false);
        isShowMenuScene = true;
        if (pauseMenu == null) {
            pauseMenu = new PauseMenuScene(onClickResume, onClickRestart, onClickExit);
        }
        pauseMenu.setTime(date,bestTime, false);
        setChildScene(pauseMenu, false, true, true);
    }

    private  ISimpleClick onClickResume = new ISimpleClick() {
        @Override
        public void onClick() {
            final BaseGameActivity activity = GameContex.getCurrent();
            if (isNotGameOver) {
                clearChildScene();
                isShowMenuScene = false;
                showHud(true);
            }else{


                if (GIFT_FOR_LIFE <= GameDateHolder.getBonuses().getBonusCount()) {
                    enabledPauseMenu = true;
                    waveController.addOvertime(4f);
                    waveController.increaseTime();
                    gameSpeed *= 0.9;
                    updateGameSpeed();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    hero.restart();
                                    isNotGameOver = true;
                                }
                            }, 2000);

                        }
                    });
                    clearChildScene();
                    GameDateHolder.getBonuses().buy(GIFT_FOR_LIFE);
                    isShowMenuScene = false;
                    showHud(true);
                }else{

                    ((Main)GameContex.getCurrent()).buy();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,activity.getString(R.string.you_have_not_gift),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };

    private ISimpleClick onClickExit =  new ISimpleClick() {
        @Override
        public void onClick() {
            saveGame();

            back();
        }
    };

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (!isShowMenuScene ) {
            if (isNotGameOver) {
                gameTime += pSecondsElapsed;
                giftTime += pSecondsElapsed;
                // todo оптимизировать по памяти
                if (updateTimerCounter < 0) {
                    date.setTime((int)(gameTime*1000));
                    timerScore.setText(Utils.timerFormat(date));
                    updateTimerCounter = UPDATE_TIMER_COUNTER_MAX;
                }

                if( GIFT_TIME_MAX < giftTime ){
                    giftTime = 0;
                    GameDateHolder.getBonuses().findOne();
                    final BaseGameActivity main = GameContex.getCurrent();
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(main, main.getString(R.string.you_get_one_dift),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                updateTimerCounter--;

                ArrayList<ICollisionObject> objects = treeCollection.haveCollision(hero);
                if (objects.size() != 0) {
                    if (flag2) {
                        flag2 = false;
                        onGameOver();
                        hero.setGameOverForm(objects.get(0));
                    }
                }else {
                    flag2 = true;
                }
            }
            waveController.update(pSecondsElapsed,isNotGameOver);
        }
        super.onManagedUpdate(pSecondsElapsed);
    }

    private void showHud(boolean flag){
        getChildByIndex(LAYERS.HUD_LAYER.ordinal()).setVisible(flag);
    }

    private void saveGame(){
       GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
               .putLong(MAX_TIME, bestTime.getTime())
               .putInt(BONUS_COUNT, GameDateHolder.getBonuses().getBonusCount())
               .commit();
    }

    private void loadGame(){
        SharedPreferences pref = GameContex.getCurrent().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        bestTime = new Date(pref.getLong(MAX_TIME,0));
        GameDateHolder.getBonuses().setBonusCount(pref.getInt(BONUS_COUNT,0));

    }

    public void updatePresent(Integer data){
        StringBuilder builder = new StringBuilder("x ").append(data);
        presentScore.setText(builder.toString());
    }
}
