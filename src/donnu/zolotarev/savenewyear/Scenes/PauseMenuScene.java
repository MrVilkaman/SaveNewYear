package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.EasyLayoutsFactory;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.HALIGMENT;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.WALIGMENT;
import donnu.zolotarev.savenewyear.Utils.Utils;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.Date;

public class PauseMenuScene extends BaseScene {


    private static final int Y_DELTA = 130;
    private Text timerScore;
    private Text bestTimerScore;
    private RectangularShape resumeButton;

    private Entity entity;

    public PauseMenuScene(ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit) {
        super();
        setBackgroundEnabled(false);
        Rectangle rectangle =  new Rectangle(0,0, Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT, GameContex.getCurrent().getVertexBufferObjectManager());
        rectangle.setColor(Color.BLACK);
        rectangle.setAlpha(0.6f);
        attachChild(rectangle);
        createButtons(onClickResume, onClickRestart, onClickExit);
    }

    private void createButtons(ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit){
        BaseGameActivity main = GameContex.getCurrent();
        String text = main.getString(R.string.pause_menu_resume);
        resumeButton = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickResume), Constants.CAMERA_WIDTH / 2 ,
                Constants.CAMERA_HEIGHT/3 + Y_DELTA, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(resumeButton);
        attachChild(resumeButton);

        Text presentScore = new Text(0, 0, TextureManager.getFont(), "= 10x", main.getVertexBufferObjectManager());
        presentScore = (Text)EasyLayoutsFactory.alihment(presentScore,resumeButton.getX()+resumeButton.getWidth(),resumeButton.getY(),WALIGMENT.LEFT, HALIGMENT.TOP);
        RectangularShape present = EasyLayoutsFactory.alihment(createSprite(TextureManager.getPresent()), presentScore.getX() +presentScore.getWidth(),presentScore.getY()-20, WALIGMENT.LEFT, HALIGMENT.TOP);


                 entity = new Entity();
        entity.attachChild(present);
        entity.attachChild(presentScore);
        attachChild(entity);
        entity.setVisible(false);


        text = main.getString(R.string.pause_menu_restart);
        RectangularShape btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickRestart), Constants.CAMERA_WIDTH / 2 ,
                Constants.CAMERA_HEIGHT/3 + resumeButton.getHeight()+30 + Y_DELTA, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

        text = main.getString(R.string.pause_menu_return_to_menu);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickExit), Constants.CAMERA_WIDTH / 2,
                Constants.CAMERA_HEIGHT / 3 +2* (btn1.getHeight()+30 ) + Y_DELTA, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

        timerScore = new Text(0,0, TextureManager.getBigFont(),"01:23.456",main.getVertexBufferObjectManager());
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,-10, WALIGMENT.CENTER, HALIGMENT.TOP);

        bestTimerScore = new Text(0,0, TextureManager.getBigFont(),GameContex.getCurrent().getString(R.string.pause_menu_best_time,"01:23.456"),main.getVertexBufferObjectManager());
        bestTimerScore = (Text)EasyLayoutsFactory.alihment( bestTimerScore
                ,Constants.CAMERA_WIDTH/2,timerScore.getHeight()+10, WALIGMENT.CENTER, HALIGMENT.TOP);
        attachChild(timerScore);
        attachChild(bestTimerScore);


    }


    @Override
    protected void initLayers() {

    }

    public void setTime(Date time, Date best, boolean isGameOver) {
        if (isGameOver) {
//            unregisterTouchArea(resumeButton);
//           detachChild(resumeButton);

        }
        entity.setVisible(isGameOver);
        timerScore.setText( Utils.timerFormat(time));
        bestTimerScore.setText(GameContex.getCurrent().getString(R.string.pause_menu_best_time, Utils.timerFormat(best)));
    }
}
