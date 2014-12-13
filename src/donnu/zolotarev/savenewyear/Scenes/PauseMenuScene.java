package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.util.color.Color;

public class PauseMenuScene extends BaseScene {


    private static final int Y_DELTA = 30;

    public PauseMenuScene(Main main,ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit) {
        super(main);
        setBackgroundEnabled(false);

        Rectangle rectangle =  new Rectangle(0,0, Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT,main.getVertexBufferObjectManager());
        rectangle.setColor(Color.BLACK);
        rectangle.setAlpha(0.6f);
        attachChild(rectangle);

        createButtons(onClickResume, onClickRestart, onClickExit);
    }

    private void createButtons(ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit){
        String text = main.getString(R.string.pause_menu_resume);
        RectangularShape btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickResume), Constants.CAMERA_WIDTH / 2 ,
                Constants.CAMERA_HEIGHT/3 , WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

        text = main.getString(R.string.pause_menu_restart);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickRestart), Constants.CAMERA_WIDTH / 2 ,
                Constants.CAMERA_HEIGHT/3 + btn1.getHeight() + Y_DELTA, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

        text = main.getString(R.string.pause_menu_return_to_menu);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickExit), Constants.CAMERA_WIDTH / 2,
                Constants.CAMERA_HEIGHT / 3 +2* (btn1.getHeight() + Y_DELTA), WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

    }


    @Override
    protected void initLayers() {

    }
}
