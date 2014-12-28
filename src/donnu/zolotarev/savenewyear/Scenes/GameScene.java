package donnu.zolotarev.savenewyear.Scenes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Activities.IAnalistyc;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.WaveController;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.MyObserver;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Utils;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Date;

public class GameScene extends BaseGameScene implements  MyObserver {


    public static final int GIFT_FOR_LIFE = 5;
    private static final float PARALLAX_CHANGE_PER_SECOND = 10;
    private  static final int UPDATE_TIMER_COUNTER_MAX = 6;


    private static final float GIFT_TIME_MAX = 45f;
    private static final long DELAY_GAMEOVER = 1000; //mc

    private static final String MAX_TIME = "MAX_TIME";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String BONUS_COUNT = "BONUS_COUNT";


    private boolean flag2 = true;

    private Text presentScore;

    private float giftTime = 0;


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


    private Date date = new Date(0);


    public GameScene(ISimpleClick onClickRestart) {
        super();
        this.onClickRestart = onClickRestart;

        createHUD();
        initOthers();
        waveController.start();

        loadGame();
        updateGameSpeed();
        updatePresent(GameDateHolder.getBonuses().getBonusCount());
   //     createFPSBase();
    }

    @Override
    protected IWaveController initWaveControllr() {
        return new WaveController(this);
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
        GameDateHolder.getBonuses().addObserver(this);
    }

    @Override
    public void update(int data) {
        updatePresent(data);
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
    public void onPause() {
        if (enabledPauseMenu){
            if (!isShowMenuScene){
                showPause();
            }
        }
    }

    @Override
    public void destroy() {
        isShowMenuScene = true;
        super.destroy();
        GameDateHolder.getBonuses().removeObserver(this);

        onClickRestart = null;
        onClickExit = null;
        onClickPause = null;
        onClickResume = null;

        if (pauseMenu != null) {
            pauseMenu.destroy();
            pauseMenu = null;
        }

        pauseMenu = null;
        timerScore = null;



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
                    GameContex.getAnalistyc().sendReport("GAME","Resume game from Bonus", IAnalistyc.GAME_TIME,date.getTime());
                    enabledPauseMenu = true;
                    waveController.addOvertime(4f);
                    waveController.increaseTime();
                    speedUp();
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
                            }, 2500);

                        }
                    });
                    clearChildScene();
                    GameDateHolder.getBonuses().buy(GIFT_FOR_LIFE);
                    isShowMenuScene = false;
                    showHud(true);
                }else{

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           new AlertDialog.Builder(activity)
                                    .setMessage(R.string.you_have_not_gift)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((Main)GameContex.getCurrent()).buy();
                                        }
                                    }).setNegativeButton(R.string.rate_no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                           // Toast.makeText(activity,activity.getString(R.string.you_have_not_gift),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };

    private ISimpleClick onClickExit =  new ISimpleClick() {
        @Override
        public void onClick() {
            GameContex.getActionResolver().submitScoreGPGS(bestTime.getTime());
            saveGame();
            GameContex.getAnalistyc().sendReport("GAME","Close gamescreen", IAnalistyc.GAME_TIME,date.getTime());
            back();
            if (Constants.NEED_ADS) {
                AdBuddiz.showAd(GameContex.getCurrent());
            }
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
                        GameDateHolder.getAchievementsHelper().proccessDieHeroAchievements(objects.get(0).whoIsThere());
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
