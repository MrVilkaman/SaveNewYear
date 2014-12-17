package donnu.zolotarev.savenewyear.BarrierWave;

import donnu.zolotarev.savenewyear.Utils.Utils;

public class WaveController implements IWaveController {

    private boolean isStart = false;
    private ICanUnitCreate unitCreate;

    private float minTime = 0.8f;
    private float maxTime = 3f;
    private float currentTime = minTime;

    public WaveController(ICanUnitCreate unitCreate) {
        this.unitCreate = unitCreate;
    }

    @Override
    public void update(float delta) {
        if (isStart) {
            currentTime -= delta;
            if(currentTime <0){
                unitCreate.initNextUnit();
                currentTime = getNewTime();
            }
        }
    }

    private float getNewTime() {
        return Utils.random(minTime,maxTime);
    }

    @Override
    public void start() {
       isStart = true;
    }

    @Override
    public void getNext() {
        currentTime = 0;
    }


}
