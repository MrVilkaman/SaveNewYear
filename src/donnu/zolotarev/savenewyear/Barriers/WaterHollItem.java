package donnu.zolotarev.savenewyear.Barriers;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Utils;
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
                if (mX < -(getWidthScaled() + 50)) {
                    destroy(false);
                }
            }
        };
        rect = new Rectangle(0, 0, he.getWidth(),he.getHeight(), gameActivity.getVertexBufferObjectManager());
        rect.setScaleCenter(he.getWidth() / 2-30, he.getHeight());
        rect.setScale(0.60f, 1.3f);
        rect.setColor(Color.BLUE);
        rect.setAlpha(0.5f);
        sprite.attachChild(rect);
        rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, true);
        sprite.setScaleCenterX(0);

        sprite.setIgnoreUpdate(true);
        sprite.setVisible(false);
        physicsHandler.setEnabled(false);
    }

    public void setStart(){
        super.setStart();
        rect.setScale(0.60f, 1.3f);
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET,575-sprite.getHeight());
        float scale = Utils.random(-10f, 10f)/100;
        sprite.setScaleX(1-scale);
    }

    @Override
    public void setStart(float offset) {
        super.setStart(offset);
        sprite.setScaleX(3f);
        rect.setScale(0.70f, 1.3f);
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.WATER_HOLL;
    }
}
