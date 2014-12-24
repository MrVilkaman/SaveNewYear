package donnu.zolotarev.savenewyear;

import android.app.Application;
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ru.elifantiev.android.roboerrorreporter.RoboErrorReporter.bindReporter(this);

    }
}
