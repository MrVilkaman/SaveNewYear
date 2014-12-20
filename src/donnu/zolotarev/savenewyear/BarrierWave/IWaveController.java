package donnu.zolotarev.savenewyear.BarrierWave;

public interface IWaveController {
    public void update(float delta);
    public void start();
    public void getNext();
    public void addOvertime(float delta);
}
