package donnu.zolotarev.savenewyear.Barriers.Menegment;

import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;

public abstract class BaseUnit implements ICollisionObject,IBarrier {
    protected Rectangle rect;
    protected PhysicsHandler physicsHandler;
    protected RectangularShape sprite;


    public void setStart(){
        SceneContext.getActiveScene().attachSelfToCollection(this);
        sprite.setPosition(Constants.CAMERA_WIDTH+50,561-sprite.getHeight());
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
}