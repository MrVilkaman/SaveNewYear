package donnu.zolotarev.savenewyear.GameData;

public class GameDateHolder {

    private static Bonuses bonuses;
    private static AchievementsHelper achievementsHelper;

    public static Bonuses getBonuses() {
        return bonuses;
    }

    public static void intit(Bonuses bonuses,AchievementsHelper achievementsHelper ) {
        GameDateHolder.bonuses = bonuses;
        GameDateHolder.achievementsHelper = achievementsHelper;
    }

    public static AchievementsHelper getAchievementsHelper() {
        return achievementsHelper;
    }

    public static void clear(){
        achievementsHelper = null;
        bonuses = null;
    }
}
