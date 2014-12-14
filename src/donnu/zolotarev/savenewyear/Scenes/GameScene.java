package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.ParallaxLayer;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class GameScene extends BaseScene {

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

    public GameScene(Main main, ISimpleClick onClickRestart) {
        super(main);
        this. onClickRestart = onClickRestart;
        TextureManager.loadGameSprites();
        createBackGround();
        createHUD();
        initOthers();
    }

    private void createHUD() {
        timerScore = new Text(0,0, TextureManager.getFont(),"00:00.00",main.getVertexBufferObjectManager());
       // timerScore.setScale(2f);
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,0, WALIGMENT.CENTER, HALIGMENT.TOP);
        attachToLayer(LAYERS.HUD_LAYER, timerScore);

        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getPauseButton()
                , main.getVertexBufferObjectManager(),null,null, onClickPause),Constants.CAMERA_WIDTH-20, 20, WALIGMENT.RIGHT, HALIGMENT.TOP);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.HUD_LAYER, btn2);
    }

    private enum LAYERS{
        GAME_LAYER,
        FRONT_LAYER,
        HUD_LAYER
    }

    private void initOthers() {

        ///todo Убрать в отдельный класс
        ITiledTextureRegion he = TextureManager.getHero();
        AnimatedSprite animatedSprite = new AnimatedSprite(200, Constants.CAMERA_HEIGHT - TextureManager.getGameFG().getHeight()-130
                , he, main.getVertexBufferObjectManager());
        int dur = 100; // hero speed anim
        animatedSprite.animate(new long[]{dur,dur,dur,dur,dur,dur},new int[]{0,1,2,3,2,1},true);
        attachToLayer(LAYERS.GAME_LAYER, animatedSprite);
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
        Sprite sprite = new Sprite(0,Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(),TextureManager.getRoad(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_SPEED, sprite));
        attachToLayer(LAYERS.GAME_LAYER,parallaxLayer);
    }

    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
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


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (!isShowMenuScene) {
            gameTime += pSecondsElapsed;
            // todo оптимизировать по памяти
                timerScore.setText(String.format("%.1f", gameTime));
        }
        super.onManagedUpdate(pSecondsElapsed);
    }
}
