package donnu.zolotarev.savenewyear.Activities;

public interface ActionResolver {

    /** Узнать статус входа пользователя */
    public boolean getSignedInGPGS();

    /** Вход */
    public void loginGPGS();

    /** Отправить результат в таблицу рекордов */
    public void submitScoreGPGS(long score);

    /** Показать Activity с таблицей рекордов */
    public void getLeaderboardGPGS();

    public void getAchievementsGPGS();
    public void unlockAchievementGPGS(int achievementId);
    public void incrementAchievementGPGS(int achievementId,int value);


    public void showAds();
    public void openAdLink();
}
