package donnu.zolotarev.savenewyear.Scenes;

import android.opengl.GLES20;
import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Activities.Main;
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
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.*;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

public class MainMenuScene extends BaseScene {
    private enum LAYERS{
        TITLE_LAYER,
        SHOW_LAYER,
        BATTON_LAYER
    }

    private ISimpleClick onClickSetting = new ISimpleClick() {
        @Override
        public void onClick() {
            GameContex.getCurrent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GameContex.getCurrent(), "Пока не готово =( ", Toast.LENGTH_SHORT).show();
                }
            });
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

    public MainMenuScene(Main main) {
        super();
        TextureManager.loadMenuSprites();
       initBackground();
       loadData();
    }

    private void loadData() {
        GameDateHolder.setBonuses(new Bonuses());
    }


    private void initShow() {
        final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH/2,300,Constants.CAMERA_WIDTH,100);
        final SpriteParticleSystem particleSystem = new SpriteParticleSystem(particleEmitter, 10, 60, 360, TextureManager.getParticlePoint(), GameContex.getCurrent().getVertexBufferObjectManager());

        particleEmitter.setCenter(500,200);
        particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(15, 22, -60, -90));
        particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 15));
        particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
        particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f, 0.0f, 0.0f));
        particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(11.5f));

        particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3.5f, 1.0f, 0.0f));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3.5f, 4.5f, 0.0f, 1.0f));
        particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 11.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(4.5f, 11.5f, 1.0f, 0.0f));
        attachToLayer(LAYERS.SHOW_LAYER,particleSystem);
      //  particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>( GLES20.GL_SRC_ALPHA,GLES20.GL_ONE));
     //   particleSystem.addParticleInitializer(new ColorParticleInitializer(Color.GREEN));
     //   particleSystem.addParticleInitializer(new AlphaParticleInitializer(1));
      //  particleSystem.addParticleInitializer(new VelocityParticleInitializer( -20, 0,100, 200));
       // particleSystem.addParticleInitializer(new RotationParticleInitializer(0.0f, 360.0f));
//        particleSystem.setBlendFunction(GLES20.GL_ONE,GLES20.GL_ONE);
       // particleSystem.addParticleModifier(new ScaleParticleModifier(1.0f, 1.2f, 0, 5));
    //    particleSystem.addParticleModifier(new ColorParticleModifier(1, 0.98f, 1, 0.96f, 1, 0.82f, 0, 3));
   //     particleSystem.addParticleModifier(new ColorParticleModifier(1, 1, 0.5f, 1, 0, 1, 4, 6));
   //     particleSystem.addParticleModifier(new AlphaParticleModifier(0, 1, 0, 1));
   //     particleSystem.addParticleModifier(new AlphaParticleModifier(1, 0, 5, 6));
//        particleSystem.addParticleModifier(new ExpireParticleInitializer(3, 6));
       /* final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(
                new RectangleParticleEmitter(Constants.CAMERA_WIDTH / 2, 0, Constants.CAMERA_WIDTH, 1),
                2, 5, 100, TextureManager.getParticlePoint(), GameContex.getCurrent().getVertexBufferObjectManager());
         particleSystem.setBlendFunction(GLES20.GL_ALPHA, GLES20.GL_ONE);
        particleSystem.addParticleInitializer(new ColorParticleInitializer<Entity>(Color.WHITE));
        particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 3, 60, 100));
        particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, 3, 5));
        particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
        particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
        particleSystem.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f, 0.5f));

        particleSystem.setBlendingEnabled(true);
        particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
        attachToLayer(LAYERS.SHOW_LAYER,particleSystem);*/
//        particleSystem.reset();
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
                , main.getVertexBufferObjectManager(),text,TextureManager.getFont(), onClickPlay), Constants.CAMERA_WIDTH / 2 - 50,
                Constants.CAMERA_HEIGHT-100, WALIGMENT.RIGHT, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachToLayer(LAYERS.BATTON_LAYER,btn1);

        text = main.getString(R.string.main_menu_setting);
        RectangularShape btn2 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                , main.getVertexBufferObjectManager(),text,TextureManager.getFont(), onClickSetting), Constants.CAMERA_WIDTH / 2 + 50, Constants.CAMERA_HEIGHT-100, WALIGMENT.LEFT, HALIGMENT.CENTER);
        registerTouchArea(btn2);
        attachToLayer(LAYERS.BATTON_LAYER,btn2);

        final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH_HALF+100,0,Constants.CAMERA_WIDTH+200,100);
        ShowflakeGenerator generator =  new ShowflakeGenerator(particleEmitter,10,20);
        generator.addParticleInitializer(new VelocityParticleInitializer( -40, -20,100, 200));
        generator.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
        generator.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f, 1.5f));
        generator.addParticleInitializer(new ExpireParticleInitializer(5f));

        attachToLayer(LAYERS.SHOW_LAYER,generator);
        //   initShow();
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

    private ISimpleClick onClickRestart =  new ISimpleClick() {
        @Override
        public void onClick() {

            clearChildScene();
            setChildScene(new GameScene(onClickRestart), false, true, true);
        }
    };
}
