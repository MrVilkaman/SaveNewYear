package donnu.zolotarev.savenewyear;

import donnu.zolotarev.savenewyear.Activities.Main;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;

public class TreeItem {

    //todo это должно передваться из сцены и быть синхронизированно с движением дорожки и другими объектами!
    private final static float MOVE_SPEED_Y = 300;
    private  Main main;

    private  PhysicsHandler physicsHandler;
    private  Sprite sprite;

    public TreeItem(Main main,IHaveGameLayers gameLayers) {
            this.main = main;

            sprite = new Sprite(Constants.CAMERA_WIDTH+50,0, TextureManager.getNewYearTree(), main.getVertexBufferObjectManager()){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (mX < -(mWidth + 50)) {
                   destroy();
                    setIgnoreUpdate(true);
                }


            }
        };
        physicsHandler = new PhysicsHandler(sprite);
        sprite.registerUpdateHandler(physicsHandler);
        gameLayers.attachToGameLayers(sprite);
        physicsHandler.setVelocityX(-MOVE_SPEED_Y);
    }

    private void destroy() {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                physicsHandler.setEnabled(false);
                sprite.unregisterUpdateHandler(physicsHandler);
                sprite.detachSelf();
                physicsHandler = null;
                sprite = null;
                main = null;
            }
        });

    }

    public void setStart(float y){
        sprite.setPosition(Constants.CAMERA_WIDTH+50,y-sprite.getHeight());
    }

}
