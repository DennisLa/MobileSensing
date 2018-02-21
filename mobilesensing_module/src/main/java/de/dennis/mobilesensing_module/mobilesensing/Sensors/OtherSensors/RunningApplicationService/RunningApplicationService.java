package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Dennis on 11.03.2017.
 */
public class RunningApplicationService {
    private AlarmManager am;
    private PendingIntent sender;
    public RunningApplicationService() {
    }
    public void startSensingRunningApps(Context context,long interval) {
        Intent i = new Intent(context, RunningApplicationListener.class);

        //sender = PendingIntent.getBroadcast(context,1, i,0);
        sender = PendingIntent.getBroadcast(context,1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 5 * 1000;//start 5 seconds after first register.

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, interval, sender);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, firstTime,
                interval, sender);
    }
    public void stopSensingRunningApplications(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
    }
}
