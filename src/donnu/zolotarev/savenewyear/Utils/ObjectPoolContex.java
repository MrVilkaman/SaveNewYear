package donnu.zolotarev.savenewyear.Utils;

import donnu.zolotarev.savenewyear.Barriers.Menegment.IBarrierCenter;

public class ObjectPoolContex {

    private static IBarrierCenter barrierCenter;

    public static IBarrierCenter getBarrierCenter() {
        return barrierCenter;
    }


    public static void setBarrierCenter(IBarrierCenter barrierCenter) {
        ObjectPoolContex.barrierCenter = barrierCenter;
    }
}
