package donnu.zolotarev.savenewyear.Barriers;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;

public class TreeItem extends BaseUnit {

    private static final float TIME_ROTATION_TREE = 1f;
    private static final float OVER_TIME = 0.5f;

    private float FRAME_TIME = 0.01f;
    private final Rectangle rect2;
    private float animTime = 0;
    private boolean needBuild = false;
    private boolean animatedFinish = false;

    public TreeItem() {
        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getTree();
        sprite = new Sprite(Constants.CAMERA_WIDTH+50,0, he, gameActivity.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mHeight + 100)) {
                    destroy(false);
                }
            }
        };
        rect = new Rectangle(0, 0, sprite.getWidth(),sprite.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect.setScale(1f, 1f);//0.75f);
        rect.setColor(Color.BLUE);
        rect.setAlpha(0.5f);
        rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);

        rect2 = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect2.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect2.setColor(Color.RED);
        rect2.setScale(0.5f, 0.1f);
        rect2.setAlpha(0.5f);
        rect2.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
        sprite.attachChild(rect2);

        sprite.setRotationCenter(he.getWidth()-20, he.getHeight()-10);
        sprite.attachChild(rect);

        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);
        sprite.setScaleCenter(sprite.getHeight()/2, sprite.getHeight());
        sprite.setScale(1.1f);

        sprite.setIgnoreUpdate(true);
        sprite.setVisible(false);
        physicsHandler.setEnabled(false);
        sprite.setRotation(90f);

    }

    @Override
    public void setStart() {
        super.setStart();
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET,591-sprite.getHeight());

        animatedFinish = true;
        needBuild = false;
//        rect.setScaleY(0.2f);
        IActiveGameScene sc = SceneContext.getActiveScene();

    }

    @Override
    public boolean checkHit(IGetShape object) {
        if (object.getShape().collidesWith(rect2)) return true;

        if (object.getShape().collidesWith(rect)) {
            SceneContext.getActiveScene().setGroudY(521);
        } else {
            SceneContext.getActiveScene().setGroudY(561);
        }
        return false;

    }

    @Override
    public void destroy(Boolean withAnimate) {
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
