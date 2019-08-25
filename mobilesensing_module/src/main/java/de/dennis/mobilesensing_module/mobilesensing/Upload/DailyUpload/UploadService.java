package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dennis on 07.04.2017.
 */
public class UploadService {
    private AlarmManager am;
    private PendingIntent sender;
    public UploadService() {
    }
    public void startUploadService(Context context,long interval) {
        Intent i = new Intent(context, UploadListener.class);

        sender = PendingIntent.getBroadcast(context,1, i, 0);

//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(new Date().getTime());
//        c.setTimeInMillis(c.getTimeInMillis() + 86400000);//Now + 1 day 3600000- just that u see that u ve made a chsnge here
//        c.set(c.YEAR,c.MONTH,c.DATE,00,00,00);
//        long firstTime = c.getTime().getTime();
//        long sirstTime = c.getTimeInMillis();//start at 00:00:00 tomorrow

        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.HOUR_OF_DAY, 1);
        long midnight = date.getTimeInMillis();

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, midnight,
                interval, sender);
    }
    public void stopUploadService(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
    }
}
