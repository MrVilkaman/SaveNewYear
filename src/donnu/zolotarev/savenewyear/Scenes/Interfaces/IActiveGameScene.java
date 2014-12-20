package donnu.zolotarev.savenewyear.Scenes.Interfaces;

import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;

public interface IActiveGameScene {
    public void attachToGameLayers(IEntity entity, boolean beforHero);
    public void attachSelfToCollection(ICollisionObject collisionObject);
    public void detachSelfFromCollection(ICollisionObject collisionObject);
    public float getGameSpeed();
    public void registerTouchArea(ITouchArea entity);
    public float getGroundY();

    public void setGroudY(float i);
}
