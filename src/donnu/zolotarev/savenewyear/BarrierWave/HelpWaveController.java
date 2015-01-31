package donnu.zolotarev.savenewyear.BarrierWave;

import java.util.PriorityQueue;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;

public class HelpWaveController extends BaseWave{


    private final IHelpCommander stop;
    private final float MAX_TIME = 3f;
    private float currentTime = MAX_TIME;
    private boolean isReady = true;

    public HelpWaveController(IHelpCommander stop) {
        super();
        this.stop = stop;
        currentTime = MAX_TIME;
    }

    @Override
    protected PriorityQueue<BarrierKind> getTurn() {
        PriorityQueue<BarrierKind> barrierTurn = new PriorityQueue<BarrierKind>();
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
        return barrierTurn;
    }

    @Override
    public void update(float delta) {
        if (isStart) {
            currentTime -= delta;

            if(currentTime <0.2 && isReady){
                isReady = false;
                stop.ready();
            }

            if(currentTime <0){
                isReady = true;
                currentTime = MAX_TIME;
                initNextUnit(barrierTurn.poll());
            }
        }
    }

    @Override
    protected void initNextUnit(BarrierKind itemType) {
        BarrierKind e = barrierTurn.peek();
        if ( e == itemType && e != BarrierKind.SHOW_BALL) {
            isReady = false;
        }
        if (itemType  == null) {
            stop.finish();
            isStart = false;
            return;
        }
        super.initNextUnit(itemType);
    }

}
