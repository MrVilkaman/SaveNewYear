package donnu.zolotarev.savenewyear.Barriers;

import donnu.zolotarev.savenewyear.TreeItem;
import org.andengine.util.adt.pool.GenericPool;

public class BarrierCenter implements IBarrierCenter {

    @Override
    public void remoteUnit(TreeItem item) {
        genericPool.recyclePoolItem(item);
    }

    @Override
    public TreeItem getUnit() {
        return genericPool.obtainPoolItem();
    }

    @Override
    public void clear() {
        genericPool.clear();
    }

    GenericPool<TreeItem> genericPool =  new GenericPool<TreeItem>() {
        @Override
        protected TreeItem onAllocatePoolItem() {
            return new TreeItem();
        }
    };
}
