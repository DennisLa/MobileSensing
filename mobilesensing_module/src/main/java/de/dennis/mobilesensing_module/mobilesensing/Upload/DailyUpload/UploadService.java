package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

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

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis() + 86400000); //Now + 1 day
        c.set(c.YEAR,c.MONTH,c.DATE,00,00,00);
        long firstTime = c.getTime().getTime();//start at 00:00:00 tomorrow

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                interval, sender);
    }
    public void stopUploadService(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
    }
}
