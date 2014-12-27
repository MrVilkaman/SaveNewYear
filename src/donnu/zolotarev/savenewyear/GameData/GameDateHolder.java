package donnu.zolotarev.savenewyear.GameData;

public class GameDateHolder {

    private static Bonuses bonuses;
    private static AchievementsHelper achievementsHelper;
    private static Setting setting;

    public static Bonuses getBonuses() {
        return bonuses;
    }

    public static void intit(Bonuses bonuses,AchievementsHelper achievementsHelper,Setting setting ) {
        GameDateHolder.bonuses = bonuses;
        GameDateHolder.achievementsHelper = achievementsHelper;
        GameDateHolder.setting = setting;
    }

    public static AchievementsHelper getAchievementsHelper() {
        return achievementsHelper;
    }

    public static Setting getSetting() {
        return setting;
    }

    public static void clear(){
        achievementsHelper = null;
        bonuses = null;
        GameDateHolder.setting = null;
    }
}
