package donnu.zolotarev.savenewyear.GameData;

import donnu.zolotarev.savenewyear.MyObserver;

import java.util.ArrayList;
import java.util.List;

public class Bonuses{

    private List<MyObserver> observers = new ArrayList<MyObserver>();
    private int bonusCount;

    public int getBonusCount() {
        return bonusCount;
    }

    public void findOne() {
        this.bonusCount++;
        notifyObservers(bonusCount);
    }

    private void notifyObservers(int bonusCount) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(bonusCount);
        }
    }

    public void setBonusCount(int bonusCount) {
        this.bonusCount = bonusCount;
    }

    public void deleteObservers() {
        observers.clear();
    }

    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }
}
