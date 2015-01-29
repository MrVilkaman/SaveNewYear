package donnu.zolotarev.savenewyear.BarrierWave;

import java.util.PriorityQueue;
import java.util.Random;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import donnu.zolotarev.savenewyear.Utils.Utils;

public class WaveController implements IWaveController {

    private static final int MAX_LEVEL = 10;
    private final Random random;
    private final PriorityQueue<BarrierKind> barrierTurn;

    private boolean isStart = false;
    private final ICanUnitCreate unitCreate;

    private float minTime = 1.2f;
    private float maxTime = 3.3f;
    private float currentTime = maxTime/2;

    private final float timeToNextUpdate = 8f;
    private float currentTimeToNextUpdate = timeToNextUpdate;

    private int currentLevel = 0;


    public WaveController(ICanUnitCreate unitCreate) {
        this.unitCreate = unitCreate;
        random = new Random();
        barrierTurn = new PriorityQueue<BarrierKind>(){
            @Override
            public BarrierKind poll() {
                return BarrierKind.TREE;
            }
        };
      //  barrierTurn = new BarrierTurn();
    }

    @Override
    public void update(float delta, boolean isNotGameOver) {
        if (isStart) {
            currentTime -= delta;
            if(currentTime <0){
                currentTime = getNewTime();
                initNextUnit();
            }
            currentTimeToNextUpdate -=delta;
            if(isNotGameOver && currentTimeToNextUpdate <0 && currentLevel != MAX_LEVEL ){
                currentLevel++;
                unitCreate.increaseGameSpeed();
                minTime *=0.93;
                maxTime *=0.89;
                currentTimeToNextUpdate = timeToNextUpdate;
            }
        }
    }

    private void initNextUnit() {
        IBarrier item;
        BarrierKind itemType = barrierTurn.poll();

        if (itemType == BarrierKind.TREE) {
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.WATER_HOLL);
            item.setStart(95);
        }else if(itemType == BarrierKind.BONUS){
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.BONUS);

            itemType = BarrierKind.NEW_YEAR_TREE;
            if (random.nextInt()%2 == 0) {
                item.setStart(80);
            }else{
                item.setStart(-200);
            }
        }

        item = ObjectPoolContex.getBarrierCenter().getUnit(itemType);
       addOvertime(item.getOverTime());
        item.setStart();

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

    @Override
    public void increaseTime() {
        currentLevel -=2;
        minTime *=1.1f;
        maxTime *=1.1f;
    }


}
