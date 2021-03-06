package donnu.zolotarev.savenewyear.Barriers;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import donnu.zolotarev.savenewyear.Utils.Utils;

@SuppressWarnings("MagicNumber")
public class ShowBallsItem extends BaseUnit {

    private static final float SPEED_MIN = 150f;
    private static final float SPEED_MAX = 400f;

    private final Rectangle rect2;
    private int currentFrame;
    private float speedX;

    public ShowBallsItem() {
        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getShowBalls();
        sprite = new AnimatedSprite(Constants.CAMERA_WIDTH+50,0, he, gameActivity.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mWidth + 50)) {
                    destroy(false);
                }
            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect2 = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect2.setColor(Color.RED);
        rect2.setAlpha(0.5f);
        rect.setScale(3.5f, 3.5f);
        rect.setColor(Color.BLUE);
        rect.setAlpha(0.5f);
        sprite.attachChild(rect2);
        sprite.attachChild(rect);
       rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
       rect2.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);
        SceneContext.getActiveScene().registerTouchArea(rect);
        Y_OFFSET = (int)sprite.getHeight()-8;
    }

    @Override
    public boolean checkHit(IGetShape object) {
        boolean flag = object.getShape().collidesWith(rect2);
        if (flag && speedX != 0f) {
            speedX = 0f;
            updateSpeed();
        }
        return flag;
    }

    @Override
    public RectangularShape getShape() {
        return rect2;
    }

    private void updateFrame(){
        AnimatedSprite animatedSprite = (AnimatedSprite)sprite;
        if (currentFrame != -1) {
            switch (currentFrame){
                case 0:
                    rect2.setScale(0.2f, 0.2f);
                    break;
                case 1:
                    rect2.setScale(0.4f, 0.4f);
                    break;
                case 2:
                    rect2.setScale(0.7f,0.7f);
                    break;

            }
            animatedSprite.setCurrentTileIndex(currentFrame--);
        } else {
            destroy(false);
        }
    }

    @Override
    public void destroy(Boolean withAnimate) {
        sprite.setPosition(9999,9999);
        super.destroy(withAnimate);
    }

    public void setStart(){
        speedX = Utils.random(SPEED_MIN, SPEED_MAX);
        super.setStart();
        currentFrame = 1; //(r<0.5)? 0 : 1;
        updateFrame();

    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.SHOW_BALL;
    }

    public void updateSpeed() {
        physicsHandler.setAngularVelocity(-speedX);
        physicsHandler.setVelocityX(-(SceneContext.getActiveScene().getGameSpeed()+speedX));
    }
}
