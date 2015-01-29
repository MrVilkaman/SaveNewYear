package donnu.zolotarev.savenewyear.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateEventProcessor {

    private final GregorianCalendar lastGameTime;
    private final GregorianCalendar nowTime;

    public DateEventProcessor(long date) {
        Date d2 = new Date(date);
        lastGameTime = new GregorianCalendar(2015,d2.getMonth(),d2.getDate(),0,0);
//        lastGameTime.add(Calendar.HOUR, 24);
        Date d = new Date();
        nowTime = new GregorianCalendar(2015,d.getMonth(),d.getDate(),d.getHours(),d.getMinutes());
    }

    public boolean isEnterToday(){
        lastGameTime.add(Calendar.HOUR, 24);
        boolean flag = nowTime.getTimeInMillis()< lastGameTime.getTimeInMillis() ;
        lastGameTime.add(Calendar.HOUR, -24);
        return flag;
    }

    public boolean isEnterYesToday(){
        nowTime.add(Calendar.HOUR, -48);
        boolean flag = nowTime.getTimeInMillis() < lastGameTime.getTimeInMillis();
        nowTime.add(Calendar.HOUR, 48);

        boolean flag2 = lastGameTime.getTimeInMillis() < nowTime.getTimeInMillis();

        return flag && flag2;
    }
}
