package donnu.zolotarev.savenewyear.BarrierWave;

import java.util.PriorityQueue;
import java.util.Random;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Barriers.WaterHollItem;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;

abstract class  BaseWave implements IWaveController{

    private static final float WATER_HOLL_OFFSET = 95;

    protected float currentTime;
    protected final Random random;
    protected final PriorityQueue<BarrierKind> barrierTurn;

    protected boolean isStart = false;

    BaseWave() {
        random = new Random();
        barrierTurn = getTurn();
    }

    protected abstract PriorityQueue<BarrierKind> getTurn();

    protected void initNextUnit( BarrierKind itemType) {
        IBarrier item;

        if (itemType == BarrierKind.TREE) {
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.WATER_HOLL);
            int pos = random.nextInt()%3;
            if (pos == 1) {
                item.setStart(WATER_HOLL_OFFSET);
            }else if (pos == 0){
                 ((WaterHollItem)item).setStart(WATER_HOLL_OFFSET,true);
            }else{
                ((WaterHollItem)item).setStart(WATER_HOLL_OFFSET,false);
            }
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

    }
}
