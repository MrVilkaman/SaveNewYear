package donnu.zolotarev.savenewyear.Barriers.Menegment;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;

import java.util.PriorityQueue;
import java.util.Random;

public class BarrierTurn extends PriorityQueue<BarrierKind> {


    private static final int MIN_TURN = 2;
    private static final int GENERATE_TURN = 5;

    public BarrierTurn() {
        super();
        generate();
    }

    @Override
    public BarrierKind poll() {
        BarrierKind barrierKind = super.poll();
        if (size() < MIN_TURN) {
           generate();
        }
        return barrierKind != null? barrierKind :BarrierKind.NEW_YEAR_TREE;
    }

    private void generate() {
        new Thread(runnable).run();
    }

    Runnable runnable = new Runnable() {
        public Random random = new Random();

        @Override
        public void run() {
            BarrierKind itemType;
            BarrierKind lastItemType = null;
            for (int i=0;i<GENERATE_TURN;i++) {
                do {
                    int ranNum = Math.abs(random.nextInt()%6);

                    if ( ranNum == 0 || ranNum == 2 || ranNum == 1 || ranNum == 5) {   // 0.66
                        if (Math.abs(random.nextInt()%2) == 0) {
                            itemType = BarrierKind.NEW_YEAR_TREE;     //0.33
                        }else{
                            itemType = BarrierKind.WATER_HOLL;      //0.33
                        }
                    }else{
                        ranNum = Math.abs(random.nextInt()%5);
                        if (ranNum == 2 ||ranNum == 4) {
                            itemType = BarrierKind.SHOW_BALL;        //0.06
                        }else if (ranNum == 0){
                            itemType = BarrierKind.BONUS;               //0.06
                        }else {
                            itemType = BarrierKind.TREE;        //0.2
                        }
                    }
                } while (itemType == lastItemType && itemType != BarrierKind.NEW_YEAR_TREE && itemType != BarrierKind.WATER_HOLL );
                lastItemType = itemType;
                offer(itemType);
            }

        }
    };
}
