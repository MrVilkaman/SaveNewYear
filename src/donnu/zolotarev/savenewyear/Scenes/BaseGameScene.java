package donnu.zolotarev.savenewyear.Scenes;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.BarrierWave.ICanUnitCreate;
import donnu.zolotarev.savenewyear.Barriers.BaseUnit;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.FallingShow.ShowflakeGenerator;
import donnu.zolotarev.savenewyear.Hero;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.ObjectCollisionController;
import donnu.zolotarev.savenewyear.Utils.ParallaxLayer;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;

public abstract class BaseGameScene extends BaseScene  implements IActiveGameScene,ICanUnitCreate {

    protected enum LAYERS{
        ROAD_LAYER,
        GAME_LAYER,
        FRONT_LAYER,
        HUD_LAYER
    }

    private static final float BACKGROUND_LAYER_SPEED = 0.6f;
    private static final float FRONT_LAYER_COEF = 1.3f;
    private static final float GAME_LAYER_COEF = 1f;
    private static final int GROUND_Y = 561;
    private static final float SPEED_COEF = 1.075f;

    private float gameSpeed = 550;
    private float gameGroundY = GROUND_Y;

    protected Hero hero;

    protected ObjectCollisionController<ICollisionObject> treeCollection;
    private ShowflakeGenerator generator;

    private ParallaxLayer parallaxFG;
    private ParallaxLayer parallaxRoad;
    private AutoParallaxBackground autoParallaxBackground;


    public BaseGameScene() {
        super();
        TextureManager.loadGameSprites();
        SceneContext.setActiveScene(this);
        hero = new Hero();
        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    hero.jump();
                    return true;
                }
                return false;
            }
        });

        treeCollection =  new ObjectCollisionController<ICollisionObject>();
    }

    @Override
    public float getGameSpeed() {
        return gameSpeed;
    }

    @Override
    public float getGroundY() {
        return gameGroundY;
    }

    @Override
    public void setGroudY(float i) {
        gameGroundY = i;
    }

    @Override
    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed  = gameSpeed;
        updateGameSpeed();
    }

    @Override
    public void increaseGameSpeed(){
        gameSpeed *=SPEED_COEF;
        updateGameSpeed();
    }

    protected void speedUp() {
        gameSpeed *= 0.9;
    }


    protected void updateGameSpeed() {
        if (Constants.SHOW_SNOW) {
            generator.setSpeed(gameSpeed);
        }
        parallaxRoad.setParallaxChangePerSecond(gameSpeed);
        parallaxFG.setParallaxChangePerSecond(gameSpeed);
        autoParallaxBackground.setParallaxChangePerSecond(gameSpeed);
        ArrayList<ICollisionObject> col = treeCollection.get();
        for (int i = col.size()-1; 0<=i;i--){
            BaseUnit it =  (BaseUnit)col.get(i);
            it.updateSpeed();
        }
    }

    @Override
    protected void initLayers() {
        for (LAYERS l:LAYERS.values()){
            attachChild(new Entity());
        }
    }

    protected void createBackGround() {
        BaseGameActivity main = GameContex.getCurrent();
        autoParallaxBackground = new AutoParallaxBackground(0f,0f,0f,gameSpeed);
        IAreaShape background = new Sprite(0,0, TextureManager.getGameBG(),main.getVertexBufferObjectManager());
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-BACKGROUND_LAYER_SPEED, background));
        setBackground(autoParallaxBackground);

        parallaxFG = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxFG.setParallaxChangePerSecond(gameSpeed);
        parallaxFG.setParallaxScrollFactor(1);
        IAreaShape frontground = new Sprite(0, Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight(), TextureManager.getGameFG(),main.getVertexBufferObjectManager());
        parallaxFG.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-FRONT_LAYER_COEF, frontground, false, 1));
        attachToLayer(LAYERS.FRONT_LAYER, parallaxFG);

        parallaxRoad = new ParallaxLayer(main.getEngine().getCamera(), true);
        parallaxRoad.setParallaxChangePerSecond(gameSpeed);
        parallaxRoad.setParallaxScrollFactor(1);
        Sprite sprite = new Sprite(0,Constants.CAMERA_HEIGHT-TextureManager.getGameFG().getHeight()-15,TextureManager.getRoad(),main.getVertexBufferObjectManager());
        parallaxRoad.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_COEF, sprite));
        attachToLayer(LAYERS.ROAD_LAYER, parallaxRoad);

        if (Constants.SHOW_SNOW) {
            final RectangleParticleEmitter particleEmitter = new RectangleParticleEmitter(Constants.CAMERA_WIDTH*3/2.f,0,Constants.CAMERA_WIDTH*3,300);
            generator =  new ShowflakeGenerator(particleEmitter,30,45);
            generator.addParticleInitializer(new VelocityParticleInitializer( -200, 200,350, 500));
            generator.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
            generator.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.5f, 1.5f));
//            generator.addParticleInitializer(new ExpireParticleInitializer(3f));
            // parallaxFG.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-GAME_LAYER_COEF,generator));
            attachToLayer(LAYERS.FRONT_LAYER, generator);
        }


    }

    protected void attachToLayer(LAYERS layer, IEntity entity){
        getChildByIndex(layer.ordinal()).attachChild(entity);
    }

    @Override
    public void attachToGameLayers(IEntity entity, boolean beforHero) {
        if (beforHero) {
            entity.setZIndex(hero.getSprite().getZIndex()-1);
        }
        attachToLayer(LAYERS.GAME_LAYER,entity);
        if (beforHero) {
            getChildByIndex(LAYERS.GAME_LAYER.ordinal()).sortChildren();
        }
    }

    @Override
    public void attachSelfToCollection(ICollisionObject collisionObject) {
        treeCollection.add(collisionObject);
    }

    @Override
    public void detachSelfFromCollection(ICollisionObject collisionObject) {
        treeCollection.remove(collisionObject);
    }

    @Override
    public void destroy() {
        SceneContext.setActiveScene(null);
        parallaxFG = null;
        parallaxRoad = null;
        autoParallaxBackground = null;
        treeCollection.cleer();
        treeCollection = null;
        generator.clear();
        generator = null;
    }
}