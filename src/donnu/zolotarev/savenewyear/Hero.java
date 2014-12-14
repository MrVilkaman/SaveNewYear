package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class Hero {

    private static final int ANIMATE_SPEED = 100; // hero speed anim
    private static final float JUMP_SPEED = 600;
    private static final float GRAVITY_SPEED = 900;

    private final AnimatedSprite animatedSprite;
    private final PhysicsHandler physicsHandler;
    private float GROUND_Y = 400;

    private boolean isFly = false;

    public Hero(Main main) {
        ITiledTextureRegion he = TextureManager.getHero();
        animatedSprite = new AnimatedSprite(200, Constants.CAMERA_HEIGHT - TextureManager.getGameFG().getHeight()-130
                , he, main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if(  GROUND_Y < mY){
                    isFly = false;
                    physicsHandler.setVelocityY(0);
                    mY = GROUND_Y;
                }else{
                    isFly = true;
                }

            }
        };
        animatedSprite.animate(new long[]{ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED},new int[]{0,1,2,3,2,1},true);

        physicsHandler = new PhysicsHandler(animatedSprite);
        animatedSprite.registerUpdateHandler(physicsHandler);
        physicsHandler.setAccelerationY(GRAVITY_SPEED);

    }

    public void jump(){
        if (!isFly) {
            animatedSprite.setY(GROUND_Y - 10);
            physicsHandler.setVelocityY(-JUMP_SPEED);
        }
    }

    public IEntity getSprite() {
        return animatedSprite;
    }
}
