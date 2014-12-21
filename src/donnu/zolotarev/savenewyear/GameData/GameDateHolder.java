package donnu.zolotarev.savenewyear.GameData;

public class GameDateHolder {

    private static Bonuses bonuses;

    public static Bonuses getBonuses() {
        return bonuses;
    }

    public static void setBonuses(Bonuses bonuses) {
        GameDateHolder.bonuses = bonuses;
    }
}
