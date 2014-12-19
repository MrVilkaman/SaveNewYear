package donnu.zolotarev.savenewyear.FallingShow;

import org.andengine.entity.particle.Particle;
import org.andengine.util.adt.pool.GenericPool;

class ShowsPool {
    private GenericPool<Particle> genericPool;

    public ShowsPool() {
        genericPool = new GenericPool<Particle>(20) {
            @Override
            protected Particle onAllocatePoolItem() {
                return new Snowflake();
            }
        };
    }

    public void remoteUnit(Particle item) {
        genericPool.recyclePoolItem(item);
    }

    public Particle getUnit() {
        return genericPool.obtainPoolItem();
    }

}
