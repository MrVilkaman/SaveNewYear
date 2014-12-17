package donnu.zolotarev.savenewyear.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.S", Locale.ENGLISH);

    public static float random(float lower, float upper){
        return Math.round(Math.random() * (upper - lower)) + lower;
    }

    public static String timerFormat(Date time) {
        return sdf.format(time);
    }
}
