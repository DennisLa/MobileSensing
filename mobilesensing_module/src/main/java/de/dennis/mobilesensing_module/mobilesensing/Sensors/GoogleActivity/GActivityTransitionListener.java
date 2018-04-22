package de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityObject;

/**
 * Created by Dennis on 09.04.2018.
 */

public class GActivityTransitionListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                if(event.getTransitionType()==0){
                    //Entering Transition
                    Log.d("GACT","Entering "+event.getActivityType());
                    SharedPreferences prefs = Module.getContext().getSharedPreferences("GActivityRecognition",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong("Starttime",event.getElapsedRealTimeNanos());
                    editor.putInt("Activity", event.getActivityType());
                    editor.apply();
                }else if (event.getTransitionType()==1){
                    //Exit Transition
                    Log.d("GACT","Exiting "+event.getActivityType());
                    SharedPreferences prefs = Module.getContext().getSharedPreferences("GActivityRecognition",Context.MODE_PRIVATE);
                    long timestamp = prefs.getLong("Starttime", new Date().getTime());
                    int activity = event.getActivityType();
                    String actName = "";
                    switch (activity) {
                        case 0: actName = "IN_VEHICLE";
                            break;
                        case 1: actName = "ON_BICYCLE";
                            break;
                        case 2: actName = "ON_FOOT";
                            break;
                        case 3: actName = "STILL";
                            break;
                        case 4: actName = "UNKNOWN";
                            break;
                        case 5: actName = "TILTING";
                            break;
                        case 7: actName = "WALKING";
                            break;
                        case 8: actName = "RUNNING";
                            break;
                    }
                    GActivityObject gao = new GActivityObject(timestamp,new Date().getTime(),actName);
                    EventBus.getDefault().post(new SensorDataEvent(gao));
                }
            }
        }
    }
}
