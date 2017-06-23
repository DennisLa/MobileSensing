package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Dennis on 04.05.2017.
 */
public class ScreenOnService {
    private AlarmManager am;
    private PendingIntent sender;
    public ScreenOnService() {
    }
    public void startSensingScreenStatus(Context context,long interval) {
        Intent i = new Intent(context, ScreenOnListener.class);

        sender = PendingIntent.getBroadcast(context,1, i, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 5 * 1000;//start 5 seconds after first register.

        am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                interval, sender);
    }
    public void stopSensingScreenStatus(){
        if(am != null && sender != null){
            am.cancel(sender);
        }
    }
}
