package donnu.zolotarev.savenewyear.Utils.EasyLayouts;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public  class EasyLayoutsFactory {
/*
    private final VertexBufferObjectManager objectManager;
    private final RectangularShape entuty;
    private final Context context;
    private HashMap<Integer,ISimpleClick> clicks;
    private Integer clickCounter;
    private int lostIndex = -1;

    public EasyLayoutsFactory(Context context, VertexBufferObjectManager objectManager) {
        clickCounter = 0;
        this.entuty = new ();
        this.objectManager = objectManager;
        this.context = context;
        clicks = new HashMap<Integer, ISimpleClick>();
    }

    public static EasyLayoutsFactory createMenu(Context context,VertexBufferObjectManager objectManager){
        return new EasyLayoutsFactory(context,objectManager);
    }


    // Картинка с нажатием
    public EasyLayoutsFactory addedItem(ITextureRegion texture, int textResurceId, IFont font, ISimpleClick simpleClick){
        reqFromClick(simpleClick);
        IMenuItem background = createSpriteItem(texture);
        IMenuItem text = createText(context.getString(textResurceId), font);
        text = alihment(text,texture.getWidth()/2,texture.getHeight()/2,WALIGMENT.CENTER,HALIGMENT.CENTER);

        background.attachChild(text);
        return addMenuItem(background);
    }
*/
/*

       // Текст без нажатиея
    public EasyLayoutsFactory addedText(String text, IFont iFont){
        return addMenuItem(createText(text, iFont));
    }


    public EasyLayoutsFactory addedText(String text, IFont iFont, float x, float y,WALIGMENT waligment, HALIGMENT haligment){
        return attachChild(createText(text, iFont,x,y,waligment,haligment));
    }
*//*


    private IMenuItem createSpriteItem(ITextureRegion texture){
        SpriteMenuItem resetMenuItem = new SpriteMenuItem(lostIndex, texture,
                objectManager);
        resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        return resetMenuItem;
    }

    private IMenuItem createText(String text, IFont iFont){
        TextMenuItem textMenuItem = new TextMenuItem(lostIndex,iFont, text, objectManager);
        textMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        textMenuItem.setColor(Color.WHITE);
        return textMenuItem;
    }

    private IMenuItem createText(String text, IFont iFont,float x, float y,WALIGMENT waligment, HALIGMENT haligment){
        IMenuItem textMenuItem = createText(text, iFont);
        return alihment(textMenuItem,x,y,waligment,haligment);
    }

    private EasyLayoutsFactory addMenuItem(IMenuItem entity){
        entuty.attachChild(entity);
        return this;
    }

    private EasyLayoutsFactory attachChild(IMenuItem entity){
        entuty.registerTouchArea(entity);
        entuty.attachChild(entity);
        return this;
    }

    private void reqFromClick(ISimpleClick simpleClick){
        clicks.put(clickCounter,simpleClick);
        lostIndex = clickCounter++;
    }

    public IAreaShape build(){
        entuty.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX,
                                             float pMenuItemLocalY) {
                ISimpleClick d = clicks.get(pMenuItem.getID());
                if (d != null) {
                    d.onClick(pMenuItem.getID());
                    return true;
                }
                return false;
            }
        });
        return entuty;
    }
*/

    public static RectangularShape create(ITiledTextureRegion textureRegio,VertexBufferObjectManager objectManager,String text, Font font, final ISimpleClick click){
        return create(textureRegio, objectManager, text, font, click,null);
    }

    public static RectangularShape create(ITiledTextureRegion textureRegio,VertexBufferObjectManager objectManager,String text, Font font, final ISimpleClick click,Float scale){
        final Sprite btn = new AnimatedSprite(0,0,textureRegio,objectManager){
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    setCurrentTileIndex(0);
                    click.onClick();
                }else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
                    if (getTileCount()!=1) {
                        setCurrentTileIndex(1);
                    }
                }else{
                    setCurrentTileIndex(0);
                }
                return true;
            }
        };
        if (scale != null) {
            btn.setScaleCenter(0,0);
            btn.setScale(scale);
        }

        if (text != null && !text.isEmpty() && font != null) {
            Text text1 = new Text(0,0,font,text,objectManager);
            text1.setPosition(btn.getWidth()/2 - text1.getWidth()/2,btn.getHeight()/2 - text1.getHeight()/2);
            btn.attachChild(text1);
        }
        return btn;
    }


    public static RectangularShape alihment(RectangularShape iMenuItem,float x, float y,WALIGMENT waligment, HALIGMENT haligment){
        int dx = 0;
        int dy = 0;
        switch (waligment){
            case LEFT:
                dx = 0;
                break;
            case CENTER:
                dx = (int)iMenuItem.getWidthScaled()/2 ;
                break;
            case RIGHT:
                dx = (int)iMenuItem.getWidthScaled();
                break;
        }

        switch (haligment){
            case TOP:
                dy = 0;
                break;
            case CENTER:
                dy = (int)iMenuItem.getHeightScaled()/2;
                break;
            case BOTTOM:
                dy = (int)iMenuItem.getHeightScaled();
                break;
        }
        iMenuItem.setPosition(x - dx,y - dy);
        return iMenuItem;
    }

}
