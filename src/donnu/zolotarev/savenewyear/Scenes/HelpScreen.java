package donnu.zolotarev.savenewyear.Scenes;

import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.BarrierWave.HelpWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.IHelpCommander;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.HelpSign.HelpSign;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.ArrayList;

public class HelpScreen extends  BaseGameScene{


    private boolean flag2 = true;
    private HelpSign tapHelp;
    private boolean isShowHelp = false;


    public HelpScreen() {
        super();
        waveController.start();
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        super.onKeyPressed(keyCode, event);
        back();
    }

    @Override
    protected IWaveController initWaveControllr() {
        return new HelpWaveController(new IHelpCommander() {
            @Override
            public void finish() {
                GameContex.getCurrent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaseGameActivity context = GameContex.getCurrent();
                        Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void addTap() {
            }

            @Override
            public void hideTap() {
            }
        });
    }



    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        ArrayList<ICollisionObject> objects = treeCollection.haveCollision(hero);
        if (objects.size() != 0) {
            if (flag2) {
                flag2 = false;
//                onGameOver();
                hero.setGameOverForm(objects.get(0));
                GameDateHolder.getAchievementsHelper().proccessDieHeroAchievements(objects.get(0).whoIsThere());
            }
        }else {
            flag2 = true;
        }

        ArrayList<ICollisionObject> col = treeCollection.get();
        if (!col.isEmpty()) {
            ICollisionObject bar = col.get(0);
//            if (bar.whoIsThere() == BarrierKind.NEW_YEAR_TREE) {
            float x = bar.getShape().getSceneCenterCoordinates()[0];
                if (!isShowHelp && x  < Constants.CAMERA_WIDTH-200) {
                    showHelp();
                }

                if (isShowHelp && x < 150) {
                    hideHelp();
                }
//            }
        }


        waveController.update(pSecondsElapsed,false);

        super.onManagedUpdate(pSecondsElapsed);
    }


    private void showHelp() {
        if (isShowHelp) {
            hideHelp();
        }
        tapHelp = HelpSign.get(HelpSign.HelpEnum.TAP);
        attachToLayer(LAYERS.HUD_LAYER,tapHelp);
        isShowHelp = true;
    }

    private void hideHelp() {
        isShowHelp = false;
        tapHelp.detachSelf();
        tapHelp = null;
    }
}
