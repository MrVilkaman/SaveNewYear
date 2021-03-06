package donnu.zolotarev.savenewyear.Barriers;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Scenes.BaseGameScene;
import donnu.zolotarev.savenewyear.Scenes.SceneContext;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import donnu.zolotarev.savenewyear.Utils.Utils;

public class WaterHollItem extends BaseUnit {

    private static final float SCALE_RANGE_L = -10f;
    private static final float SCALE_RANGE_R = 10f;

    @SuppressWarnings("MagicNumber")
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
        Y_OFFSET = (int)sprite.getHeight()-15;
    }

    @SuppressWarnings("MagicNumber")
    public void setStart(){
        super.setStart();
        rect.setScale(0.60f, 1.3f);
        sprite.setPosition(Constants.CAMERA_WIDTH+START_X_OFFSET, BaseGameScene.GROUND_Y-Y_OFFSET);
        float scale = Utils.random(SCALE_RANGE_L, SCALE_RANGE_R)/100;
        sprite.setScaleX(1-scale);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public void setStart(float offset) {
        super.setStart(offset);
        sprite.setScaleX(2.85f);
        rect.setScale(0.70f, 1.3f);
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.WATER_HOLL;
    }

    public float getSize(){
        return sprite.getWidthScaled();
    }

    @SuppressWarnings("MagicNumber")
    public void setStart(float offset, boolean b) {
        sprite.setScaleX(2.85f);
        rect.setScale(0.70f, 1.3f);
        super.setStart(offset + (b ?sprite.getWidthScaled()/-2.5f:sprite.getWidthScaled()/1.9f));
        sprite.setScaleX(2.85f);
        rect.setScale(0.70f, 1.3f);
    }
}
