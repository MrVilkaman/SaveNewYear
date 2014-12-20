package donnu.zolotarev.savenewyear.Barriers;

public interface IBarrier {
    public BarrierKind getKind();
    public float getOverTime();
    public void setStart();
    public void setStart(float offset);
}
