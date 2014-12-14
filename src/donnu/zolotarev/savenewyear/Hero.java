package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class Hero {

    private static final float HERO_X = 150;

    private static final int ANIMATE_SPEED = 100; // hero speed anim
    private static final float JUMP_SPEED = 600;
    private static final float GRAVITY_SPEED = 900;

    private final AnimatedSprite animatedSprite;
    private final PhysicsHandler physicsHandler;
    private final Sprite shedow;

    // todo убрать константу!
    private float groundY = 561;

    private boolean isFly = false;

    public Hero(Main main,IHaveGameLayers gameLayers) {
        ITiledTextureRegion he = TextureManager.getHero();
        animatedSprite = new AnimatedSprite(HERO_X, groundY
                , he, main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                // todo проверка по двум точкам
                final float herY = groundY - mHeight;
                if(  herY < mY){
                        shedow.setScaleX(1f);
                        physicsHandler.setVelocityY(0);
                        mY = herY;
                        physicsHandler.setAccelerationY(0);
                    isFly = false;
                }else{
                        physicsHandler.setAccelerationY(GRAVITY_SPEED);
                    shedow.setScale(1-(herY - mY) / herY);
                }

            }
        };
        animatedSprite.animate(new long[]{ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED},new int[]{0,1,2,3,2,1},true);
        shedow = new Sprite(HERO_X+15, groundY -20,TextureManager.getHeroShedow(),main.getVertexBufferObjectManager());

        physicsHandler = new PhysicsHandler(animatedSprite);
        animatedSprite.registerUpdateHandler(physicsHandler);

        gameLayers.attachToGameLayers(shedow);
        gameLayers.attachToGameLayers(animatedSprite);
    }

    public void jump(){
        if (!isFly) {
            animatedSprite.setY(groundY - animatedSprite.getHeight() - 10);
            physicsHandler.setVelocityY(-JUMP_SPEED);
        }
        isFly = true;

    }

    public IEntity getSprite() {
        return animatedSprite;
    }

    public void setGroundY(float groundY) {
        this.groundY = groundY;
    }
}
