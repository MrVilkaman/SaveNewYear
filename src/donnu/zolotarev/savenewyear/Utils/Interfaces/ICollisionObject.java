package donnu.zolotarev.savenewyear.Utils.Interfaces;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;

public interface ICollisionObject extends IDestroy,IGetShape {
    public boolean checkHit(IGetShape object);

    public BarrierKind whoIsThere();
}

