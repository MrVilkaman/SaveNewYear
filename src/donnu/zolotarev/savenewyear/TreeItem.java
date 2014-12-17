package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.GameContex;
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

public class TreeItem  implements ICollisionObject {

    //todo это должно передваться из сцены и быть синхронизированно с движением дорожки и другими объектами!
    private final static float MOVE_SPEED_Y = 500;
    private final Rectangle rect;
//   private  Main main;

    private  PhysicsHandler physicsHandler;
    private  RectangularShape sprite;

    public TreeItem() {

        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getNewYearTree();
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
        rect.setScale(0.4f, 0.75f);
        rect.setColor(Color.BLUE);
        sprite.attachChild(rect);
        rect.setVisible(false);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite);
        physicsHandler.setVelocityX(-MOVE_SPEED_Y);
    }

    public void setStart(float y){
        SceneContext.getActiveScene().attachSelfToCollection(this);
        sprite.setPosition(Constants.CAMERA_WIDTH+50,y-sprite.getHeight());
        sprite.setIgnoreUpdate(false);
        sprite.setVisible(true);
        physicsHandler.setEnabled(true);
    }

    @Override
    public boolean checkHit(IGetShape object) {
        return object.getShape().collidesWith(sprite);
    }

    @Override
    public void destroy(Boolean withAnimate) {
        sprite.setIgnoreUpdate(true);
        sprite.setVisible(false);
        physicsHandler.setEnabled(false);
        ObjectPoolContex.getBarrierCenter().remoteUnit(this);
     /* GameContex.getCurrent().runOnUiThread(new Runnable() {
          //  main.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              physicsHandler.setEnabled(false);
              sprite.unregisterUpdateHandler(physicsHandler);
              sprite.detachSelf();
              physicsHandler = null;
              sprite = null;

//                main = null;
          }
      });*/
        SceneContext.getActiveScene().detachSelfFromCollection(this);
    }

    @Override
    public RectangularShape getShape() {
        return rect;
    }
}
