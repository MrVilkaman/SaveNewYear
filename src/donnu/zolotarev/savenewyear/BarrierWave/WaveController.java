package donnu.zolotarev.savenewyear.BarrierWave;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;
import donnu.zolotarev.savenewyear.Utils.Utils;

import java.util.Random;

public class WaveController implements IWaveController {

    private static final int MAX_LEVEL = 10;
    private final Random random;

    private boolean isStart = false;
    private ICanUnitCreate unitCreate;

    private float minTime = 1.2f;
    private float maxTime = 3.3f;
    private float currentTime = maxTime/2;

    private float timeToNextUpdate = 7f;
    private float currentTimeToNextUpdate = timeToNextUpdate;

    private int currentLevel = 0;
    private BarrierKind lastItemType;


    public WaveController(ICanUnitCreate unitCreate) {
        this.unitCreate = unitCreate;
        random = new Random();
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
                maxTime *=0.91;
                currentTimeToNextUpdate = timeToNextUpdate;
            }
        }
    }

    private void initNextUnit() {
        IBarrier item;
        BarrierKind itemType;
        do {
            int ranNum = Math.abs(random.nextInt()%6);

            if ( ranNum == 0 || ranNum == 2 || ranNum == 1 || ranNum == 5) {   // 0.66
                if (Math.abs(random.nextInt()%2) == 0) {
                    itemType = BarrierKind.NEW_YEAR_TREE;     //0.3
                }else{
                    itemType = BarrierKind.WATER_HOLL;      //0.3
                }
            }else{
                ranNum = Math.abs(random.nextInt()%5);
                if (ranNum == 2) {
                    itemType = BarrierKind.SHOW_BALL;        //0.06
                }else if (ranNum == 3){
                    itemType = BarrierKind.BONUS;               //0.06
                }else {
                    itemType = BarrierKind.TREE;        //0.2
                }
            }
        } while (itemType == lastItemType && itemType != BarrierKind.NEW_YEAR_TREE && itemType != BarrierKind.WATER_HOLL );
       /* double r;
        do {
            r = Math.random();
            if(r<0.25) {
                itemType = BarrierKind.NEW_YEAR_TREE;
            }else if (r<0.5){
                itemType = BarrierKind.WATER_HOLL;
            }else if (r<0.75){
                itemType = BarrierKind.SHOW_BALL;
            }else if (r<0.85){
                itemType = BarrierKind.BONUS;
            }else {
                itemType = BarrierKind.TREE;
            }
        } while (itemType == lastItemType);*/

        lastItemType = itemType;

        if (itemType == BarrierKind.TREE) {
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.WATER_HOLL);
            item.setStart(70);
        }else if(itemType == BarrierKind.BONUS){
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.BONUS);

            itemType = BarrierKind.NEW_YEAR_TREE;
            if (random.nextInt()%2 == 0) {
                item.setStart(70);
            }else{
                item.setStart(-190);
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


}
