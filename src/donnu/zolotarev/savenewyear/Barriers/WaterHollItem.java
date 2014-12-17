package donnu.zolotarev.savenewyear.Barriers;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.ObjectPoolContex;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

public class WaterHollItem implements ICollisionObject,IBarrier {

    //todo это должно передваться из сцены и быть синхронизированно с движением дорожки и другими объектами!
    private final static float MOVE_SPEED_Y = 500;
    private final Rectangle rect;
//   private  Main main;

    private  PhysicsHandler physicsHandler;
    private  RectangularShape sprite;

    public WaterHollItem() {

        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getWaterHoll();
           sprite = new Sprite(Constants.CAMERA_WIDTH+50,0, he, gameActivity.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mWidth + 50)) {
                   destroy(false);
                }
            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect.setScale(0.95f, 1.8f);
        rect.setColor(Color.BLUE);
        rect.setAlpha(0.5f);
        sprite.attachChild(rect);
        rect.setVisible(false);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);
        physicsHandler.setVelocityX(-MOVE_SPEED_Y);
    }

    public void setStart(){
        SceneContext.getActiveScene().attachSelfToCollection(this);
        sprite.setPosition(Constants.CAMERA_WIDTH+50,561-sprite.getHeight()+10);
        sprite.setIgnoreUpdate(false);
        sprite.setVisible(true);
        physicsHandler.setEnabled(true);
    }

    @Override
    public boolean checkHit(IGetShape object) {
        return object.getShape().collidesWith(rect);
    }

    @Override
    public void destroy(Boolean withAnimate) {
        sprite.setIgnoreUpdate(true);
        sprite.setVisible(false);
        physicsHandler.setEnabled(false);
        ObjectPoolContex.getBarrierCenter().remoteUnit(this);
        SceneContext.getActiveScene().detachSelfFromCollection(this);
    }

    @Override
    public RectangularShape getShape() {
        return rect;
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.WATER_HOLL;
    }
}
