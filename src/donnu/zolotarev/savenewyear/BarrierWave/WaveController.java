package donnu.zolotarev.savenewyear.BarrierWave;

import donnu.zolotarev.savenewyear.Utils.Utils;

public class WaveController implements IWaveController {

    private static final int MAX_LEVEL = 10;

    private boolean isStart = false;
    private ICanUnitCreate unitCreate;

    private float minTime = 0.9f;
    private float maxTime = 3f;
    private float currentTime = maxTime/2;

    private float timeToNextUpdate = 4f;
    private float currentTimeToNextUpdate = timeToNextUpdate;

    private int currentLevel = 0;


    public WaveController(ICanUnitCreate unitCreate) {
        this.unitCreate = unitCreate;
    }

    @Override
    public void update(float delta) {
        if (isStart) {
            currentTime -= delta;
            if(currentTime <0){
                currentTime = getNewTime();
                unitCreate.initNextUnit();
            }
            currentTimeToNextUpdate -=delta;
            if(currentTimeToNextUpdate <0 && currentLevel != MAX_LEVEL ){
                currentLevel++;
                unitCreate.updateGameSpeed();
                minTime *=0.95;
                maxTime *=0.95;
                currentTimeToNextUpdate = timeToNextUpdate;
            }
        }
    }

    private float getNewTime() {
        return Utils.random(minTime, maxTime);
    }

    @Override
    public void start() {
       isStart = true;
    }

    @Override
    public void getNext() {
        currentTime = 0;
    }

    @Override
    public void addOvertime(float delta) {
        currentTime +=delta;
    }


}
