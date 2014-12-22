package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IActiveGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import donnu.zolotarev.savenewyear.Utils.Utils;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

public class Hero implements ICollisionObject{

    private static final float HERO_X = 150;

    private static final int ANIMATE_SPEED = 110; // hero speed anim
    private static final float JUMP_SPEED = 1120;
    private static final float GRAVITY_SPEED = 3750;
    private static final float GRAVITY_SPEED_MAX = 10000;

    private final AnimatedSprite animatedSprite;
    private final PhysicsHandler physicsHandler;
    private final Sprite shedow;
    private final Rectangle rect;
    private final PhysicsHandler shedowPhysicsHandler;

    // todo убрать константу!
    private float groundY = 561;

    private boolean isFly = false;
    private boolean die = false;
    private boolean dieInWaterHoll = false;
    private boolean dieDelay = false;

    public Hero() {
        ITiledTextureRegion he = TextureManager.getHero();
        BaseGameActivity main = GameContex.getCurrent();
        final IActiveGameScene gameLayers = SceneContext.getActiveScene();

        animatedSprite = new AnimatedSprite(HERO_X,gameLayers.getGroundY()  , he, main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                // todo проверка по двум точкам
                final float herY = gameLayers.getGroundY() - mHeight;
                if(  herY < mY && !dieInWaterHoll){
                        shedow.setScaleX(1f);
                        physicsHandler.setVelocityY(0);
                        mY = herY;
                    physicsHandler.setAccelerationY(GRAVITY_SPEED);
                    isFly = false;
                }else{

                    shedow.setScale(1 - (herY - mY) / herY);
                }
                if (!dieInWaterHoll) {
                    shedow.setVisible(Utils.equals(gameLayers.getGroundY(), 561,10f));
                }

                if(HERO_X < mX){
                    mX =  HERO_X;
                    physicsHandler.setVelocityX(0);
                    shedowPhysicsHandler.setVelocityX(0);
                    shedow.setX(HERO_X+15);
                }
                if (!die && (HERO_X/3 < mX)) {
                    dieDelay = false;
                }

            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), main.getVertexBufferObjectManager()){
            @Override
            public boolean collidesWith(IShape pOtherShape) {
                return !dieDelay && super.collidesWith(pOtherShape);
            }
        };
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight() / 2);
        rect.setScale(0.50f, 0.6f);
        rect.setColor(Color.GREEN);
        rect.setAlpha(0.5f);
        rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
        animatedSprite.attachChild(rect);
      //  animateStart();
        shedow = new Sprite(HERO_X+15, gameLayers.getGroundY() -20,TextureManager.getHeroShedow(),main.getVertexBufferObjectManager());

        shedowPhysicsHandler = new PhysicsHandler(shedow);
        shedow.registerUpdateHandler(shedowPhysicsHandler);

        physicsHandler = new PhysicsHandler(animatedSprite);
        animatedSprite.registerUpdateHandler(physicsHandler);

        gameLayers.attachToGameLayers(shedow, isFly);
        gameLayers.attachToGameLayers(animatedSprite, isFly);
        physicsHandler.setAccelerationY(GRAVITY_SPEED);
        restart();
    }

    private void animateStart() {
        animatedSprite.animate(new long[]{ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED},new int[]{0,1,2,3,2,1},true);
    }

    public void jump(){
        if (!die) {
            if (!isFly) {
                animatedSprite.setY(SceneContext.getActiveScene().getGroundY() - animatedSprite.getHeight() - 40);
                physicsHandler.setVelocityY(-JUMP_SPEED);
                physicsHandler.setAccelerationY(GRAVITY_SPEED);
            } else{
                physicsHandler.setAccelerationY(GRAVITY_SPEED_MAX);

            }
            isFly = true;
        }

    }

    public IEntity getSprite() {
        return rect;
    }

    public void setGroundY(float groundY) {
        this.groundY = groundY;
    }

    @Override
    public boolean checkHit(IGetShape object) {
        return object.getShape().collidesWith(rect);
    }

    @Override
    public BarrierKind whoIsThere() {
        return null;
    }

    @Override
    public void destroy(Boolean withAnimate) {

    }

    @Override
    public RectangularShape getShape() {
        return rect;
    }

    public void setGameOverForm(ICollisionObject collisionObject) {
        die = true;
        dieDelay = true;
        animatedSprite.stopAnimation(2);
        BarrierKind who = collisionObject.whoIsThere();
        physicsHandler.setVelocityX(-SceneContext.getActiveScene().getGameSpeed());
        if (who == BarrierKind.WATER_HOLL) {
            dieInWaterHoll = true;
            physicsHandler.setVelocityX(-SceneContext.getActiveScene().getGameSpeed()*0.1f);
            shedow.setVisible(false);
        }else{
            shedowPhysicsHandler.setVelocityX(-SceneContext.getActiveScene().getGameSpeed());
        }
    }

    public void restart() {
        die = false;
        dieInWaterHoll = false;
        shedow.setVisible(true);
        animatedSprite.setX(-(animatedSprite.getScaleX()+130));
        shedow.setX(-(animatedSprite.getScaleX()+130)+15);
        float v = SceneContext.getActiveScene().getGameSpeed()*0.2f;
        physicsHandler.setVelocityX(v);
        shedowPhysicsHandler.setVelocityX(v);
        animateStart();
    }
}
