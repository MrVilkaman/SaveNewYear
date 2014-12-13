package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.ParallaxLayer;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;

public class GameScene extends BaseScene {

    private enum LAYERS{
        GAME_LAYER,
        FRONT_LAYER
    }

    public GameScene(Main main) {
        super(main);


        TextureManager.loadGameSprites();
        initLayers();
        createBackGround();
        initOther();

    }

    private void initOther() {
        ParallaxLayer parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(8);
        parallaxLayer.setParallaxScrollFactor(1);
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-30.0f, frontground));
        attachToLayer(LAYERS.FRONT_LAYER,parallaxLayer);

        parallaxLayer = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxLayer.setParallaxChangePerSecond(8);
        parallaxLayer.setParallaxScrollFactor(1);
        Sprite sprite = new Sprite(0,Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(),TextureManager.getRoad(),main.getVertexBufferObjectManager());
        parallaxLayer.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-20.0f, sprite));
        attachToLayer(LAYERS.GAME_LAYER,parallaxLayer);
    }

    private void initLayers() {
        attachChild(new Entity());
        attachChild(new Entity());
    }

    private void createBackGround() {
        AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,15);
        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
       autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-4.0f, background));
        setBackground(autoParallaxBackground);
    }

    private void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }
}
