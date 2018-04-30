package de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;

/**
 * Created by Dennis on 31.03.2018.
 */

public class GActivityListener extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            String actName = "";
            switch (result.getMostProbableActivity().getType()) {
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
            Log.d("Activity Google",actName+" "+result.getMostProbableActivity().getConfidence());
           ActivityObject act = new ActivityObject(result.getTime(),actName,result.getMostProbableActivity().getConfidence());
            EventBus.getDefault().post(new SensorDataEvent(act));
        }
    }
}
