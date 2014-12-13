package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;

public class MainMenuScene extends BaseScene {
    public MainMenuScene(Main main) {
        super(main);
        TextureManager.loadMenuSprites();
       initBackground();
    }

    private void initBackground() {
        setBackground(new SpriteBackground(new Sprite(0,0, TextureManager.getMenuBG(),main.getVertexBufferObjectManager())));

    }
}
