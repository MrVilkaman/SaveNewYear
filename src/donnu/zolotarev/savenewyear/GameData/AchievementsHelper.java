package donnu.zolotarev.savenewyear.GameData;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.R;

public class AchievementsHelper {

    private static final int UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_1 = 30000;
    private static final int UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_2 = 90000;
    private static final int UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_3 = 180000;
    private static final int UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_4 = 300000;

    private static final int UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_1 = 10;
    private static final int UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_2 = 30;
    private static final int UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_3 = 75;
    private static final int UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_4 = 200;

    public void proccessDieHeroAchievements(BarrierKind kind){
        switch (kind){
            case WATER_HOLL:
                GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_walrus, 1);
                break;
            case TREE:
                GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_blocked_path,1);
                break;
            case SHOW_BALL:
                GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_snowballs_is_a_bad_game,1);
                break;
        }
    }

    public void proccessBestTime(long time){
        if ( UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_1 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_start_of_way);
        }
        if ( UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_2 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_artful_runner);
        } if ( UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_3 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_die_hard);
        } if ( UNLOCK_ACHIEVEMENT_BEST_TIME_LEVEL_4 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_cheater);
        }
    }


    public void proccessUpdateBonus(){
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_a_lover_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_a_great_lover_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_gather_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_hunter_for_gifts, 1);
        Bonuses bonuses = GameDateHolder.getBonuses();
        if (bonuses.getBonusCount() >= UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_1) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_thrifty);
        } if (bonuses.getBonusCount() >= UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_2) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_for_a_rainy_day);
        } if (bonuses.getBonusCount() >= UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_3) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_gifts_keeper);
        } if (bonuses.getBonusCount() >= UNLOCK_ACHIEVEMENT_BONUS_COUNT_LEVEL_4) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_collectors);
        }
    }

    public void proccessFromPurchase() {
        GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_generous);
    }
}
