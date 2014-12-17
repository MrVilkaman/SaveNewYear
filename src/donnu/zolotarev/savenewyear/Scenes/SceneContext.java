package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;

public class SceneContext {

    private static IActiveGameScene activeScene;

    public static IActiveGameScene getActiveScene() {
        return activeScene;
    }

    public static void setActiveScene(IActiveGameScene activeScene) {
        SceneContext.activeScene = activeScene;
    }
}
