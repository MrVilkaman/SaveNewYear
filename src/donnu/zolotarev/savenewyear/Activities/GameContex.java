package donnu.zolotarev.savenewyear.Activities;

import org.andengine.ui.activity.BaseGameActivity;

public class GameContex {

    private static BaseGameActivity gameActivity;

    public static void setGameActivity(BaseGameActivity gameActivity) {
        GameContex.gameActivity = gameActivity;
    }

    public static BaseGameActivity getCurrent() {
        return gameActivity;
    }

    public static ActionResolver getActionResolver() {
        return (ActionResolver)gameActivity;
    }

    public static IAnalistyc getAnalistyc() {
        return (IAnalistyc)gameActivity;
    }
}
