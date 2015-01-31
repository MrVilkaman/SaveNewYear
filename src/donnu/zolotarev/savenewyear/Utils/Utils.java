package donnu.zolotarev.savenewyear.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final float ACCURACY = 0.0001f;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.S", Locale.ENGLISH);

    public static float random(float lower, float upper){
        return Math.round(Math.random() * (upper - lower)) + lower;
    }

    public static String timerFormat(Date time) {
        return sdf.format(time);
    }

    public static boolean equals(float numb1,float numb2){
        return equals(numb1,numb2, ACCURACY);
    }

    public static boolean equals(float numb1,float numb2, float accuracy){
        return (Math.abs(numb1 - numb2) <= accuracy);
    }
}
