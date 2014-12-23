package donnu.zolotarev.savenewyear.Activities;

import org.andengine.ui.activity.BaseGameActivity;

public class GameContex {

    private static BaseGameActivity gameActivity;
    private static ActionResolver actionResolver;

    public static void setGameActivity(BaseGameActivity gameActivity) {
        GameContex.gameActivity = gameActivity;
    }

    public static BaseGameActivity getCurrent() {
        return gameActivity;
    }

    public static ActionResolver getActionResolver() {
        return actionResolver;
    }

    public static void setActionResolver(ActionResolver actionResolver) {
        GameContex.actionResolver = actionResolver;
    }
}
