package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.IHaveGameLayers;

public class SceneContext {

    private static IHaveGameLayers activeScene;

    public static IHaveGameLayers getActiveScene() {
        return activeScene;
    }

    public static void setActiveScene(IHaveGameLayers activeScene) {
        SceneContext.activeScene = activeScene;
    }
}
