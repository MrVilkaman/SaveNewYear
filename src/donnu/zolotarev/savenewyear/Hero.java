package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Scenes.Interfaces.IHaveGameLayers;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import donnu.zolotarev.savenewyear.Utils.Interfaces.IGetShape;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.LinkedList;

public class Hero implements ICollisionObject{

    private static final float HERO_X = 150;

    private static final int ANIMATE_SPEED = 100; // hero speed anim
    private static final float JUMP_SPEED = 900;
    private static final float GRAVITY_SPEED = 2000;
    private static final float GRAVITY_SPEED_MAX = 10000;

    private final AnimatedSprite animatedSprite;
    private final PhysicsHandler physicsHandler;
    private final Sprite shedow;
    private final Rectangle rect;

    // todo убрать константу!
    private float groundY = 561;

    private boolean isFly = false;

    public Hero() {
        ITiledTextureRegion he = TextureManager.getHero();
        BaseGameActivity main = GameContex.getCurrent();

        new ArrayList<String>();
        new LinkedList<String>();
        animatedSprite = new AnimatedSprite(HERO_X, groundY, he, main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                // todo проверка по двум точкам
                final float herY = groundY - mHeight;
                if(  herY < mY){
                        shedow.setScaleX(1f);
                        physicsHandler.setVelocityY(0);
                        mY = herY;
                    physicsHandler.setAccelerationY(GRAVITY_SPEED);
                    isFly = false;
                }else{

                    shedow.setScale(1-(herY - mY) / herY);
                }

            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), main.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, 0);
        rect.setScale(0.60f, 0.8f);
        rect.setColor(Color.GREEN);
        rect.setAlpha(0.5f);
 //       rect.setVisible(false);
        animatedSprite.attachChild(rect);
        animatedSprite.animate(new long[]{ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED, ANIMATE_SPEED},new int[]{0,1,2,3,2,1},true);
        shedow = new Sprite(HERO_X+15, groundY -20,TextureManager.getHeroShedow(),main.getVertexBufferObjectManager());

        physicsHandler = new PhysicsHandler(animatedSprite);
        animatedSprite.registerUpdateHandler(physicsHandler);

        IHaveGameLayers gameLayers = SceneContext.getActiveScene();
        gameLayers.attachToGameLayers(shedow, isFly);
        gameLayers.attachToGameLayers(animatedSprite, isFly);
    }

    public void jump(){
        if (!isFly) {
            animatedSprite.setY(groundY - animatedSprite.getHeight() - 10);
            physicsHandler.setVelocityY(-JUMP_SPEED);
            physicsHandler.setAccelerationY(GRAVITY_SPEED);
        } else{
            physicsHandler.setAccelerationY(GRAVITY_SPEED_MAX);

        }
        isFly = true;

    }

    public IEntity getSprite() {
        return animatedSprite;
    }

    public void setGroundY(float groundY) {
        this.groundY = groundY;
    }

    @Override
    public boolean checkHit(IGetShape object) {
        return  object.getShape().collidesWith(rect);
    }

    @Override
    public void destroy(Boolean withAnimate) {

    }

    @Override
    public RectangularShape getShape() {
        return rect;
    }
}
