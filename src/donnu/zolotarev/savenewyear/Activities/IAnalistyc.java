package donnu.zolotarev.savenewyear.Activities;

public interface IAnalistyc {
    String GAME_TIME = "GAME_TIME";
    String WHO_IS_ME = "WHO_IS_ME";

    public void sendReport(String message,String key, String lable,long value);
    public void sendReport(String message,String key, String value);
    public void sendReport(String message);
}
