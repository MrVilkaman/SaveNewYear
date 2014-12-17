package donnu.zolotarev.savenewyear;

import org.andengine.ui.activity.BaseGameActivity;

public class GameContex {

    private static BaseGameActivity gameActivity;

    public static void setGameActivity(BaseGameActivity gameActivity) {
        GameContex.gameActivity = gameActivity;
    }

    public static BaseGameActivity getCurrent() {
        return gameActivity;
    }
}
