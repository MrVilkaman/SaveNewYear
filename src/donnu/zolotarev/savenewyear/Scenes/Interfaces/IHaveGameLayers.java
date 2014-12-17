package donnu.zolotarev.savenewyear.Scenes.Interfaces;

import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import org.andengine.entity.IEntity;

public interface IHaveGameLayers {
    public void attachToGameLayers(IEntity entity, boolean beforHero);
    public void attachSelfToCollection(ICollisionObject collisionObject);
    public void detachSelfFromCollection(ICollisionObject collisionObject);
}