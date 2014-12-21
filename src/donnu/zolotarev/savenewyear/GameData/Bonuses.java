package donnu.zolotarev.savenewyear.GameData;

import java.util.Observable;

public class Bonuses  extends Observable{

    private int bonusCount;

    public int getBonusCount() {
        return bonusCount;
    }

    public void findOne() {
        this.bonusCount++;
        setChanged();
        notifyObservers(bonusCount);
    }

    public void setBonusCount(int bonusCount) {
        this.bonusCount = bonusCount;
    }
}
