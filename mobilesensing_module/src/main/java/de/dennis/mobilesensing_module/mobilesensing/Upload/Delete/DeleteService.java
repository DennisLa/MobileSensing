package de.dennis.mobilesensing_module.mobilesensing.Upload.Delete;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload.UploadListener;

/**
 * Created by Dennis on 29.10.2017.
 */

public class DeleteService {
    private AlarmManager am;
    private PendingIntent sender;
    public DeleteService() {
    }
    public void startDeleteService(Context context, long interval) {
        Intent i = new Intent(context, DeleteListener.class);

        //sender = PendingIntent.getBroadcast(context,1, i, 0);
        sender = PendingIntent.getBroadcast(context,1, i, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis() + 86400000); //Now + 1 day
        c.set(c.YEAR,c.MONTH,c.DATE,00,00,00);
        long firstTime = c.getTime().getTime();//start at 00:00:00 tomorrow

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, interval, sender);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, firstTime, interval, sender);
    }
    public void stopDeleteService(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
}}
