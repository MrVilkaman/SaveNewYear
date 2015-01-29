package donnu.zolotarev.savenewyear.Barriers;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;

import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;

public abstract class BaseUnit implements ICollisionObject,IBarrier {
    protected static final int START_X_OFFSET = 150;
    protected Rectangle rect;
    protected PhysicsHandler physicsHandler;
    protected RectangularShape sprite;


    public void setStart(){
        updateSpeed();
        IActiveGameScene scene = SceneContext.getActiveScene();
        scene.attachSelfToCollection(this);
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET,561-sprite.getHeight());
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

    public void updateSpeed() {
        physicsHandler.setVelocityX(-SceneContext.getActiveScene().getGameSpeed());
    }

    @Override
    public float getOverTime() {
        return 0;
    }

    @Override
    public void setStart(float offset) {
        setStart();
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET + offset,575-sprite.getHeight());
    }

    @Override
    public void setStart(float offset, float offsetY) {
        setStart();
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET + offset,575-sprite.getHeight()+offsetY);
    }

    @Override
    public BarrierKind whoIsThere() {
        return getKind();
    }
}
