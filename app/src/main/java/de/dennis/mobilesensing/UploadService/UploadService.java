package de.dennis.mobilesensing.UploadService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

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
        c.set(c.YEAR,c.MONTH,c.DATE,23,59,59);
        long firstTime = c.getTime().getTime();//start at 23:59:59 today

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
