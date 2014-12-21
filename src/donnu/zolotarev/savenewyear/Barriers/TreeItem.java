package donnu.zolotarev.savenewyear.Barriers;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBounceOut;

public class TreeItem extends BaseUnit {

    private static final float TIME_ROTATION_TREE = 1f;
    private static final float OVER_TIME = 2f;

    private float FRAME_TIME = 0.01f;
    private final Rectangle rect2;
    private float animTime = 0;
    private boolean needBuild = false;
    private boolean animatedFinish = false;
    private float defSpeed;

    public TreeItem() {
        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getTree();
        sprite = new AnimatedSprite(Constants.CAMERA_WIDTH+50,0, he, gameActivity.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mHeight + 50)) {
                    destroy(false);
                }
                if (needBuild && !animatedFinish) {
                    animTime += pSecondsElapsed;
                    if (FRAME_TIME < animTime  ) {
                        animTime = 0;

                        AnimatedSprite animatedSprite = (AnimatedSprite)sprite;
                        if (animatedSprite.getCurrentTileIndex() < animatedSprite.getTileCount()-1) {
                            animatedSprite.setCurrentTileIndex(animatedSprite.getCurrentTileIndex()+1);
                        }else{
                            animatedFinish = true;
                            registerEntityModifier(new RotationModifier(TIME_ROTATION_TREE,0,90,EaseBounceOut.getInstance()));
                        }
                        rect.setScaleY(1f*animatedSprite.getCurrentTileIndex()/animatedSprite.getTileCount());
                    }
                    needBuild = false;
                }
            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect.setScale(1f, 0.2f);//0.75f);
        rect.setColor(Color.BLUE);
        rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);

        rect2 = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager()){

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
                    needBuild = true;
                }
                return true;
            }
        };
        // rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect2.setColor(Color.RED);
        rect2.setAlpha(0.5f);
        rect2.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
//        rect2.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect2.setY(he.getHeight()/3);
        rect2.resetRotationCenter();
        rect2.setScale(2.4f, 0.6f);

        sprite.setRotationCenter(he.getWidth()-20, he.getHeight()-10);
        sprite.attachChild(rect);
        sprite.attachChild(rect2);

        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);
        SceneContext.getActiveScene().registerTouchArea(rect2);
        sprite.setScaleCenterY(sprite.getHeight());
        sprite.setScaleY(1.1f);

       //((AnimatedSprite)sprite).setCurrentTileIndex(7);
    }

    @Override
    public void setStart() {
        sprite.setRotation(0);
        super.setStart();
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET,581-sprite.getHeight());
        ((AnimatedSprite)sprite).setCurrentTileIndex(0);
        animatedFinish = false;
        needBuild = false;
        rect.setScaleY(0.2f);
        IActiveGameScene sc = SceneContext.getActiveScene();
        defSpeed = sc.getGameSpeed();
        sc.setGameSpeed(defSpeed < 900?defSpeed :900);
    }

    @Override
    public boolean checkHit(IGetShape object) {
        if (object.getShape().collidesWith(rect)) {
            if (animatedFinish){
                SceneContext.getActiveScene().setGroudY(521);
            }else{
                return true;
            }
//            return false;
        } else {
            if (animatedFinish){
                SceneContext.getActiveScene().setGroudY(561);
            }
        }
        return false;

    }

    @Override
    public void destroy(Boolean withAnimate) {
        SceneContext.getActiveScene().setGameSpeed(defSpeed);
        sprite.setRotation(0);
        super.destroy(withAnimate);
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.TREE;
    }

    @Override
    public float getOverTime() {
        return OVER_TIME;
    }
}
