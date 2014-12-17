package donnu.zolotarev.savenewyear.Utils;

public class Utils {

    public static float random(float lower, float upper){
        return Math.round(Math.random() * (upper - lower)) + lower;
    }
}
