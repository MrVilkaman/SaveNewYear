package donnu.zolotarev.savenewyear.FallingShow;

import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.Textures.TextureManager;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;

public class Snowflake extends Particle<Sprite> {
    public Snowflake() {
        setEntity(new TiledSprite(0, 0, TextureManager.getParticlePoint(), GameContex.getCurrent().getVertexBufferObjectManager()));
    }
}
