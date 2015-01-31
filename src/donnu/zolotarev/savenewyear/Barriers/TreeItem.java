package donnu.zolotarev.savenewyear.Barriers;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.BaseGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;

public class TreeItem extends BaseUnit {

    private static final float OVER_TIME = 0.9f;
    private static final int TREE_OFFSET = 40;

    private final Rectangle rect2;

    @SuppressWarnings("MagicNumber")
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
        Y_OFFSET = (int)sprite.getHeight()-35;

    }

    @Override
    public boolean checkHit(IGetShape object) {
        if (object.getShape().collidesWith(rect2)) return true;

        if (object.getShape().collidesWith(rect)) {
            SceneContext.getActiveScene().setGroudY(BaseGameScene.GROUND_Y- TREE_OFFSET);
        } else {
            SceneContext.getActiveScene().setGroudY(BaseGameScene.GROUND_Y);
        }
        return false;
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
