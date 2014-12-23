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

public class NewYearTreeItem extends BaseUnit {

    public NewYearTreeItem() {

        BaseGameActivity gameActivity = GameContex.getCurrent();
        ITiledTextureRegion he = TextureManager.getNewYearTree();
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
        rect.setScale(0.4f, 0.75f);
        rect.setColor(Color.BLUE);
        sprite.attachChild(rect);
        rect.setVisible(Constants.SHOW_COLLAPS_ITEM_ZONE);
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        SceneContext.getActiveScene().attachToGameLayers(sprite, false);
        sprite.setScaleCenter(sprite.getHeight()/2, sprite.getHeight());

        sprite.setIgnoreUpdate(true);
        sprite.setVisible(false);
        physicsHandler.setEnabled(false);

    }

    @Override
    public void setStart() {
        super.setStart();
        float scale = Utils.random(-10f, 10f)/100;
        sprite.setScale(1-scale,1+scale);
    }

    @Override
    public void setStart(float offset) {
        float scale = Utils.random(-10f, 10f)/100;
        sprite.setScale(1-scale,1+scale);
        super.setStart(offset);
    }

    @Override
    public BarrierKind getKind() {
        return BarrierKind.NEW_YEAR_TREE;
    }
}
