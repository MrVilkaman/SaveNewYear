package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActivityCallback;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;

public class MainMenuScene extends BaseScene {
    private enum LAYERS{
        TITLE_LAYER,
        SHOW_LAYER,
        BATTON_LAYER
    }

    private ISimpleClick onClickSetting = new ISimpleClick() {
        @Override
        public void onClick() {
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(main, "Пока не готово =( ", Toast.LENGTH_SHORT).show();
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
        super(main);
        TextureManager.loadMenuSprites();
       initBackground();
    }

    @Override
    protected void initLayers() {
        attachChild(new Entity());
        attachChild(new Entity());
        attachChild(new Entity());
    }

    private void initBackground() {
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
    }



    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        if (getChildScene() != null) {
            ((IActivityCallback)getChildScene()).onKeyPressed(keyCode, event);

        }else{
            main.finish();
        }
    }

    private ISimpleClick onClickRestart =  new ISimpleClick() {
        @Override
        public void onClick() {
            clearChildScene();
            setChildScene(new GameScene(main,onClickRestart), false, true, true);
        }
    };
}
