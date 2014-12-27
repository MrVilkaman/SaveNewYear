package donnu.zolotarev.savenewyear.BarrierWave;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Barriers.IBarrier;
import donnu.zolotarev.savenewyear.Utils.ObjectPoolContex;

import java.util.PriorityQueue;
import java.util.Random;

public class HelpWaveController implements IWaveController{


    private final PriorityQueue<BarrierKind> barrierTurn;
    private final Random random;
    private final IHelpCommander stop;
    private boolean isStart = false;

    private float maxTime = 3f;
    private float currentTime = maxTime;
    private boolean isReady = true;

    public HelpWaveController(IHelpCommander stop) {
        this.stop = stop;
        random = new Random();
        barrierTurn = new PriorityQueue<BarrierKind>();
        barrierTurn.add(BarrierKind.NEW_YEAR_TREE);
        barrierTurn.add(BarrierKind.NEW_YEAR_TREE);
        barrierTurn.add(BarrierKind.WATER_HOLL);
        barrierTurn.add(BarrierKind.WATER_HOLL);
        barrierTurn.add(BarrierKind.TREE);
        barrierTurn.add(BarrierKind.TREE);
        barrierTurn.add(BarrierKind.SHOW_BALL);
        barrierTurn.add(BarrierKind.SHOW_BALL);
        barrierTurn.add(BarrierKind.SHOW_BALL);
        barrierTurn.add(BarrierKind.BONUS);
        barrierTurn.add(BarrierKind.BONUS);
    }

    @Override
    public void update(float delta, boolean isNotGameOver) {
        if (isStart) {
            currentTime -= delta;

            if(currentTime <0.2 && isReady){
                isReady = false;
                stop.ready();
            }

            if(currentTime <0){
                isReady = true;
                currentTime = maxTime;
                initNextUnit();
            }
        }
    }

    @Override
    public void start() {
        isStart = true;
    }

    @Override
    public void getNext() {

    }

    @Override
    public void addOvertime(float delta) {

    }

    @Override
    public void increaseTime() {

    }

    private void initNextUnit() {
        IBarrier item;
        BarrierKind itemType = barrierTurn.poll();
        BarrierKind e = barrierTurn.peek();
        if ( e == itemType && e != BarrierKind.SHOW_BALL) {
            isReady = false;
        }
        if (itemType  == null) {
            stop.finish();
            isStart = false;
            return;
        }
        if (itemType == BarrierKind.TREE) {
            item = ObjectPoolContex.getBarrierCenter().getUnit(BarrierKind.WATER_HOLL);
            item.setStart(70);
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
}
