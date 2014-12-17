package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;

public class TreeItem  implements ICollisionObject {

    //todo это должно передваться из сцены и быть синхронизированно с движением дорожки и другими объектами!
    private final static float MOVE_SPEED_Y = 500;
    private final IHaveGameLayers gameLayers;
    private final Rectangle rect;
    private  Main main;

    private  PhysicsHandler physicsHandler;
    private  RectangularShape sprite;

    public TreeItem(Main main,IHaveGameLayers gameLayers) {
            this.main = main;
        this.gameLayers = gameLayers;

        ITiledTextureRegion he = TextureManager.getNewYearTree();
           sprite = new Sprite(Constants.CAMERA_WIDTH+50,0, he, main.getVertexBufferObjectManager()){
//            sprite = new Rectangle(Constants.CAMERA_WIDTH+50,0, he.getWidth(),he.getHeight(), main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mWidth + 50)) {
                   destroy(false);
                    setIgnoreUpdate(true);
                }


            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), main.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect.setScale(0.4f, 0.75f);
        rect.setColor(Color.BLUE);
        sprite.attachChild(rect);
        rect.setVisible(false);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        gameLayers.attachToGameLayers(sprite);
        gameLayers.attachSelfToCollection(this);
        physicsHandler.setVelocityX(-MOVE_SPEED_Y);
    }

    public void setStart(float y){
        sprite.setPosition(Constants.CAMERA_WIDTH+50,y-sprite.getHeight());
    }

    @Override
    public boolean checkHit(IGetShape object) {
        return object.getShape().collidesWith(sprite);
    }

    @Override
    public void destroy(Boolean withAnimate) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                physicsHandler.setEnabled(false);
                sprite.unregisterUpdateHandler(physicsHandler);
                sprite.detachSelf();
                physicsHandler = null;
                sprite = null;
                main = null;
            }
        });
        gameLayers.detachSelfFromCollection(this);
    }

    @Override
    public RectangularShape getShape() {
        return rect;
    }
}
