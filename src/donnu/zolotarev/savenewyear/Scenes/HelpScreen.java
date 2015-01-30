package donnu.zolotarev.savenewyear.Scenes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.Toast;
import donnu.zolotarev.savenewyear.Activities.GameContex;
import donnu.zolotarev.savenewyear.BarrierWave.HelpWaveController;
import donnu.zolotarev.savenewyear.BarrierWave.IHelpCommander;
import donnu.zolotarev.savenewyear.BarrierWave.IWaveController;
import donnu.zolotarev.savenewyear.Barriers.BarrierKind;
import donnu.zolotarev.savenewyear.Constants;
import donnu.zolotarev.savenewyear.GameData.GameDateHolder;
import donnu.zolotarev.savenewyear.HelpSign.HelpSign;
import donnu.zolotarev.savenewyear.R;
import donnu.zolotarev.savenewyear.Utils.EasyLayouts.ISimpleClick;
import donnu.zolotarev.savenewyear.Utils.Interfaces.ICollisionObject;

import java.util.ArrayList;

public class HelpScreen extends  BaseGameScene{


    private static boolean inFirst = true;
    private ISimpleClick onClickRestart;
    private boolean flag2 = true;
    private HelpSign tapHelp;
    private boolean isShowHelp = false;
    private boolean isPause = false;


    public HelpScreen(ISimpleClick onClickRestart) {
        super();
        this.onClickRestart = onClickRestart;
        waveController.start();

        if (inFirst) {
            inFirst = false;
            GameContex.getCurrent().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isPause = true;
                    new AlertDialog.Builder(GameContex.getCurrent())
                            .setMessage(R.string.tutorials)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isPause = false;
                                }
                            }).show();
                }
            });
        }
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
                        new AlertDialog.Builder(GameContex.getCurrent())
                                .setMessage(R.string.help_congratulations)
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GameContex.getCurrent().runOnUpdateThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                GameDateHolder.getSetting().setNeedTutorials(false);
                                                onClickRestart.onClick();
                                            }
                                        });
                                    }
                                }).show();
                                    }
                                });
            }

            @Override
            public void ready() {
                GameContex.getCurrent().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  isPause = true;
                        Toast.makeText(GameContex.getCurrent(),R.string.get_ready,Toast.LENGTH_SHORT).show();
                       /* new AlertDialog.Builder(GameContex.getCurrent())
                                .setMessage(R.string.get_ready)
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isPause = false;
                                    }
                                }).show();*/
                    }
                });
            }


            @Override
            public void hideTap() {
            }
        });
    }

    @Override
    public void destroy() {
        isPause = true;
        super.destroy();
        onClickRestart = null;
        tapHelp = null;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (!isPause) {
            ArrayList<ICollisionObject> objects = treeCollection.haveCollision(hero);
            if (objects.size() != 0) {
                if (flag2) {
                    flag2 = false;
                    onGameOver();
                    hero.setGameOverForm(objects.get(0));
                    GameDateHolder.getAchievementsHelper().proccessDieHeroAchievements(objects.get(0).whoIsThere());
                }
            }else {
                flag2 = true;
            }

            ArrayList<ICollisionObject> col = treeCollection.get();
            if (!col.isEmpty()) {
                ICollisionObject bar = col.get(0);

                    if (bar.whoIsThere() == BarrierKind.SHOW_BALL) {
                        if (!isShowHelp) {
                            showHelp(HelpSign.HelpEnum.TAP);
                        }
                        if (tapHelp != null) {
                            float[] pos = bar.getShape().getSceneCenterCoordinates();
                            tapHelp.setPosition(pos[0],pos[1]);
                        }
                    } else if (2<= col.size() && col.get(1).whoIsThere()== BarrierKind.TREE){
                        if (!isShowHelp) {
                            showHelp(HelpSign.HelpEnum.HOLD);
                        }
                        if (tapHelp != null) {
                            float[] pos = bar.getShape().getSceneCenterCoordinates();
                            tapHelp.setPosition(pos[0]-280,pos[1]-50);
                        }
                    } else {
                        float x = bar.getShape().getSceneCenterCoordinates()[0];
                        if (!isShowHelp && x  < Constants.CAMERA_WIDTH-200) {
                            showHelp(HelpSign.HelpEnum.TAP);
                        }

                        if (isShowHelp && x < 150) {
                            hideHelp();
                        }
                    }

            }else{
                hideHelp();
            }


            waveController.update(pSecondsElapsed);

            super.onManagedUpdate(pSecondsElapsed);
        }
    }

    private void onGameOver() {
        GameContex.getCurrent().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isPause = true;
                new AlertDialog.Builder(GameContex.getCurrent())
                        .setMessage(R.string.try_again)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameContex.getCurrent().runOnUpdateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onClickRestart.onClick();
                                    }
                                });
                            }
                        }).show();
            }
        });

    }


    private void showHelp(HelpSign.HelpEnum helpSign) {
        if (isShowHelp) {
            hideHelp();
        }
        tapHelp = HelpSign.get(helpSign);
        attachToLayer(LAYERS.HUD_LAYER,tapHelp);
        isShowHelp = true;
    }

    private void hideHelp() {
        if (isShowHelp) {
            isShowHelp = false;
            tapHelp.detachSelf();
            tapHelp = null;
        }
    }
}
