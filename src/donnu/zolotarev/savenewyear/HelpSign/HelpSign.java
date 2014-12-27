package donnu.zolotarev.savenewyear.HelpSign;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

public class HelpSign extends Sprite {

    public enum HelpEnum{
        TAP,
        HOLD
    }

    public static HelpSign get(HelpEnum anEnum){
        if (anEnum == HelpEnum.TAP) {
            return new HelpSign(TextureManager.getTapHelp());
        }else {
            return new HelpSign(TextureManager.getHoldHelp());
        }

    }

    public HelpSign(ITextureRegion pTextureRegion) {
        super(Constants.CAMERA_WIDTH_HALF+250, Constants.CAMERA_HEIGHT - pTextureRegion.getHeight(), pTextureRegion, GameContex.getCurrent().getVertexBufferObjectManager());
    }


}
