package donnu.zolotarev.savenewyear.Barriers.Menegment;

import java.util.PriorityQueue;
import java.util.Random;

import donnu.zolotarev.savenewyear.Barriers.BarrierKind;

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
//                do {
                    int ranNum = Math.abs(random.nextInt()%5);

                    if ( ranNum == 0 || ranNum == 4 || ranNum == 1 ) {   // 0.60
                        if (Math.abs(random.nextInt()%2) == 0) {
                            itemType = BarrierKind.NEW_YEAR_TREE;     //0.30
                        }else{
                            itemType = BarrierKind.WATER_HOLL;      //0.30
                        }
                    }else{
                        ranNum = Math.abs(random.nextInt()%3);
                        if (ranNum == 2 ) {
                            itemType = BarrierKind.SHOW_BALL;        //0.13
                        }else if (ranNum == 0){
                            itemType = BarrierKind.BONUS;               //0.13
                        }else {
                            itemType = BarrierKind.TREE;        //0.13
                        }
                    }
//                } while (itemType == lastItemType && itemType != BarrierKind.NEW_YEAR_TREE && itemType != BarrierKind.WATER_HOLL );
                lastItemType = itemType;
                offer(itemType);
            }

        }
    };
}
