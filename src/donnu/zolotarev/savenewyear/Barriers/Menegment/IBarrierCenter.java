package donnu.zolotarev.savenewyear.Barriers.Menegment;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;

public interface IBarrierCenter {

    void remoteUnit(IBarrier item);
    public IBarrier getUnit(BarrierKind kind);
    public void clear();

  }
