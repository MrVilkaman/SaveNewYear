package donnu.zolotarev.savenewyear.Barriers.Menegment;

import donnu.zolotarev.savenewyear.Barriers.*;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.adt.pool.MultiPool;

public class BarrierCenter implements IBarrierCenter {

    private MultiPool<IBarrier> genericPool;

    public BarrierCenter() {
        genericPool = new MultiPool<IBarrier>();
        genericPool.registerPool(BarrierKind.TREE.ordinal(),new GenericPool<IBarrier>() {
            @Override
            protected IBarrier onAllocatePoolItem() {
                return new TreeItem();
            }
        });
        genericPool.registerPool(BarrierKind.WATER_HOLL.ordinal(),new GenericPool<IBarrier>() {
            @Override
            protected IBarrier onAllocatePoolItem() {
                return new WaterHollItem();
            }
        });
        genericPool.registerPool(BarrierKind.SHOW_BALL.ordinal(),new GenericPool<IBarrier>() {
            @Override
            protected IBarrier onAllocatePoolItem() {
                return new ShowBallsItem();
            }
        });
    }

    @Override
    public void remoteUnit(IBarrier item) {
        genericPool.recyclePoolItem(item.getKind().ordinal(),item);
    }

    @Override
    public IBarrier getUnit(BarrierKind kind) {

        return genericPool.obtainPoolItem(kind.ordinal());
    }

    @Override
    public void clear() {
        genericPool.clear();
    }


}
