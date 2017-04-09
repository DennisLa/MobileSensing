package de.dennis.mobilesensing.TrackService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Dennis on 21.03.2017.
 */
public class TrackService {
    private AlarmManager am;
    private PendingIntent sender;
    public TrackService() {
    }
    public void startTrackSensing(Context context,long interval) {
        Intent i = new Intent(context, TrackListener.class);

        sender = PendingIntent.getBroadcast(context,1, i, 0);

        Calendar c = Calendar.getInstance();
        c.set(c.YEAR,c.MONTH,c.DATE,23,59,59);
        Long firstTime = c.getTime().getTime();//start at 23:59:59 today

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                interval, sender);
    }
    public void stopSensingTracks(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
    }
}
