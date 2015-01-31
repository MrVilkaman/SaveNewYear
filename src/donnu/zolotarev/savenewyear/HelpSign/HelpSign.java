package donnu.zolotarev.savenewyear.HelpSign;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Textures.TextureManager;

public class HelpSign extends Sprite {

    public static HelpSign get(){
            return new HelpSign(TextureManager.getTapHelp());
    }

    @SuppressWarnings("MagicNumber")
    public HelpSign(ITextureRegion pTextureRegion) {
        super(Constants.CAMERA_WIDTH_HALF+250, Constants.CAMERA_HEIGHT - pTextureRegion.getHeight(), pTextureRegion, GameContex.getCurrent().getVertexBufferObjectManager());

    }

}
