package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Barriers.IBarrierCenter;

public class ObjectPoolContex {

    private static IBarrierCenter barrierCenter;

    public static IBarrierCenter getBarrierCenter() {
        return barrierCenter;
    }


    public static void setBarrierCenter(IBarrierCenter barrierCenter) {
        ObjectPoolContex.barrierCenter = barrierCenter;
    }
}
