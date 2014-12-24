package donnu.zolotarev.savenewyear.GameData;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.R;

public class AchievementsHelper {

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
        if ( 30000 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_start_of_way);
        }
        if ( 90000  <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_artful_runner);
        } if ( 180000 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_die_hard);
        } if ( 300000 <=time) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_cheater);
        }
    }


    public void proccessUpdateBonus(){
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_a_lover_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_a_great_lover_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_gather_of_gifts, 1);
        GameContex.getActionResolver().incrementAchievementGPGS(R.string.achievement_hunter_for_gifts, 1);
        Bonuses bonuses = GameDateHolder.getBonuses();
        if (bonuses.getBonusCount() >= 10) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_thrifty);
        } if (bonuses.getBonusCount() >= 30) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_for_a_rainy_day);
        } if (bonuses.getBonusCount() >= 75) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_gifts_keeper);
        } if (bonuses.getBonusCount() >= 200) {
            GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_collectors);
        }
    }

    public void proccessFromPurchase() {
        GameContex.getActionResolver().unlockAchievementGPGS(R.string.achievement_generous);
    }
}
