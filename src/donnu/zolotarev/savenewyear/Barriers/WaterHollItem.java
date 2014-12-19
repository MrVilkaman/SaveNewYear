package donnu.zolotarev.savenewyear.Barriers;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

public class WaterHollItem extends BaseUnit {


    public WaterHollItem() {
        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getWaterHoll();
           sprite = new Sprite(Constants.CAMERA_WIDTH+50,0, he, gameActivity.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mWidth + 50)) {
                   destroy(false);
                }
            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2, he.getHeight());
        rect.setScale(0.8f, 1.4f);
        rect.setColor(Color.BLUE);
        rect.setAlpha(0.5f);
        sprite.attachChild(rect);
        rect.setVisible(false);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);

    }

    public void setStart(){
        super.setStart();
        sprite.setPosition(Constants.CAMERA_WIDTH+50,575-sprite.getHeight());
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.WATER_HOLL;
    }
}
