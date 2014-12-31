package donnu.zolotarev.savenewyear.Scenes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.AppUtils;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.MyObserver;
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
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.Date;

public class PauseMenuScene extends BaseScene implements MyObserver {


    private static final int Y_DELTA = 70;
    private static Date time;
    private Text timerScore;
    private Text bestTimerScore;
    private RectangularShape resumeButton;
    private Text presentScoreView;

    private Entity entity;
    private ISimpleClick onHowToPlay = new ISimpleClick() {
        @Override
        public void onClick() {
            GameContex.getCurrent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseGameActivity context = GameContex.getCurrent();
                    new AlertDialog.Builder(context).setMessage(R.string.how_to_play_text).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                }
            });
        }
    };

    public PauseMenuScene(ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit) {
        super();
        setBackgroundEnabled(false);
        Rectangle rectangle =  new Rectangle(0,0, Constants.CAMERA_WIDTH,Constants.CAMERA_HEIGHT, GameContex.getCurrent().getVertexBufferObjectManager());
        rectangle.setColor(Color.BLACK);
        rectangle.setAlpha(0.8f);
        attachChild(rectangle);
        createButtons(onClickResume, onClickRestart, onClickExit);
    }

    private void createButtons(ISimpleClick onClickResume, ISimpleClick onClickRestart, ISimpleClick onClickExit){
        final BaseGameActivity main = GameContex.getCurrent();

        String text = main.getString(R.string.pause_menu_restart);
        RectangularShape btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickRestart), Constants.CAMERA_WIDTH / 2 ,
                Constants.CAMERA_HEIGHT/2 + Y_DELTA, WALIGMENT.CENTER, HALIGMENT.CENTER);
        registerTouchArea(btn1);
        attachChild(btn1);

         text = main.getString(R.string.pause_menu_resume);
        resumeButton = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickResume,0.7f), Constants.CAMERA_WIDTH - 30,
                Constants.CAMERA_HEIGHT - 30, WALIGMENT.RIGHT, HALIGMENT.BOTTOM);
        registerTouchArea(resumeButton);
        attachChild(resumeButton);

        RectangularShape present = EasyLayoutsFactory.alihment(createSprite(TextureManager.getPresent()), resumeButton.getX() - 110,resumeButton.getY()-10, WALIGMENT.RIGHT, HALIGMENT.TOP);
        Text presentScore = new Text(0, 0, TextureManager.getFont(), "x "+GameScene.GIFT_FOR_LIFE+" = ", main.getVertexBufferObjectManager());
        presentScore = (Text)EasyLayoutsFactory.alihment(presentScore,present.getX()+present.getWidth(),present.getY()+5,WALIGMENT.LEFT, HALIGMENT.TOP);


                 entity = new Entity();
        entity.attachChild(present);
        entity.attachChild(presentScore);
        attachChild(entity);
        entity.setVisible(false);
        text = main.getString(R.string.pause_menu_how_to_play);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onHowToPlay,0.7f), Constants.CAMERA_WIDTH / 2,
                resumeButton.getY()-20, WALIGMENT.CENTER, HALIGMENT.BOTTOM);
        registerTouchArea(btn1);
        attachChild(btn1);

        text = main.getString(R.string.pause_menu_return_to_menu);
        btn1 = EasyLayoutsFactory.alihment(EasyLayoutsFactory.create(TextureManager.getButtons()
                        , main.getVertexBufferObjectManager(), text, TextureManager.getFont(), onClickExit,0.7f), 30,
                Constants.CAMERA_HEIGHT - 30, WALIGMENT.LEFT, HALIGMENT.BOTTOM);
        registerTouchArea(btn1);
        attachChild(btn1);

        text = main.getString(R.string.share_btn_text);
        Text text1 = new Text(0,0,TextureManager.getFont(),text,main.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    AppUtils.share(GameContex.getCurrent(),main.getString(R.string.share_text,Utils.timerFormat(time)));
                }
                return true;
            }
        };
        text1.setPosition(Constants.CAMERA_WIDTH -10 - text1.getWidth(),10);

        registerTouchArea(text1);
        attachChild(text1);


        timerScore = new Text(0,0, TextureManager.getBigFont(),"01:23.456",main.getVertexBufferObjectManager());
        timerScore = (Text)EasyLayoutsFactory.alihment( timerScore
                ,Constants.CAMERA_WIDTH/2,-10, WALIGMENT.CENTER, HALIGMENT.TOP);

        bestTimerScore = new Text(0,0, TextureManager.getBigFont(),GameContex.getCurrent().getString(R.string.pause_menu_best_time,"01:23.456"),main.getVertexBufferObjectManager());
        bestTimerScore = (Text)EasyLayoutsFactory.alihment( bestTimerScore
                ,Constants.CAMERA_WIDTH/2,timerScore.getHeight()-5, WALIGMENT.CENTER, HALIGMENT.TOP);
        attachChild(timerScore);
        attachChild(bestTimerScore);

        RectangularShape present2 = EasyLayoutsFactory.alihment(createSprite(TextureManager.getPresent()),5,8,WALIGMENT.LEFT, HALIGMENT.TOP);

        presentScoreView = new Text(0,0, TextureManager.getFont(),"x 000",main.getVertexBufferObjectManager());
        presentScoreView = (Text)EasyLayoutsFactory.alihment( presentScoreView
                ,present2.getX()+present2.getWidth()+5, present2.getY() +present2.getHeight()+10, WALIGMENT.LEFT, HALIGMENT.BOTTOM);
        attachChild(present2);
        attachChild(presentScoreView);

    }

    @Override
    protected void initLayers() {

    }

    public void setTime(Date time, Date best, boolean isGameOver) {
        if (isGameOver) {
//            unregisterTouchArea(resumeButton);
//           detachChild(resumeButton);

        }
        GameDateHolder.getBonuses().addObserver(this);
        update(GameDateHolder.getBonuses().getBonusCount());
        entity.setVisible(isGameOver);
        timerScore.setText(Utils.timerFormat(time));
        PauseMenuScene.time = best;
        bestTimerScore.setText(GameContex.getCurrent().getString(R.string.pause_menu_best_time, Utils.timerFormat(best)));
    }

    @Override
    public void destroy() {
        GameDateHolder.getBonuses().removeObserver(this);
        onHowToPlay = null;
        entity.detachChildren();
        entity.detachSelf();
        clearTouchAreas();
        clearEntityModifiers();
        clearUpdateHandlers();
        clearChildScene();
        time = null;
    }

    @Override
    public void update(int data) {
        StringBuilder builder = new StringBuilder("x ").append(data);
        presentScoreView.setText(builder.toString());
    }
}
