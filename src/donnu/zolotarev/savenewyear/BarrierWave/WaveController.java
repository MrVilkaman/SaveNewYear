package donnu.zolotarev.savenewyear.BarrierWave;

import java.util.PriorityQueue;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Utils.Utils;

public class WaveController extends BaseWave {

    private static final int MAX_LEVEL = 10;
    private final ICanUnitCreate unitCreate;

    private float minTime = 1.3f;
    private float maxTime = 3.0f;


    private final float timeToNextUpdate = 8f;
    private float currentTimeToNextUpdate = timeToNextUpdate;

    private int currentLevel = 0;


    public WaveController(ICanUnitCreate unitCreate) {
        super();
        this.unitCreate = unitCreate;
        currentTime = maxTime/2;
    }

    @Override
    protected PriorityQueue<BarrierKind> getTurn() {
//        return new BarrierTurn();
        return new PriorityQueue<BarrierKind>(){
            @Override
            public BarrierKind poll() {
                return BarrierKind.TREE;
            }
        };
    }

    @Override
    public void update(float delta) {
        if (isStart) {
            currentTime -= delta;
            if(currentTime <0){
                currentTime = getNewTime();
                initNextUnit(barrierTurn.poll());
            }
            currentTimeToNextUpdate -=delta;
            if(currentTimeToNextUpdate <0 && currentLevel != MAX_LEVEL ){
                currentLevel++;
                unitCreate.increaseGameSpeed();
                minTime *=0.955;
                maxTime *=0.93;
                currentTimeToNextUpdate = timeToNextUpdate;
            }
        }
    }


    @Override
    public void increaseTime() {
        currentLevel -=2;
        minTime *=1.1f;
        maxTime *=1.1f;
    }

    protected float getNewTime() {
        return Utils.random(minTime, maxTime);
    }
}
