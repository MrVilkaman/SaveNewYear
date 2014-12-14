package donnu.zolotarev.savenewyear.Utils.Interfaces;

public interface ICollisionObject extends IDestroy,IGetShape {
    public boolean checkHit(IGetShape object);
}

