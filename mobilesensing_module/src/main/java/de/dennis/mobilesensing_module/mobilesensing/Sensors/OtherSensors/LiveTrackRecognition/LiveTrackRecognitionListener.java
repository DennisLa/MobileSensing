package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.LiveTrackRecognition;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.TrackEndEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.ms.ptenabler.Message.ClusterMessage;

/**
 * Created by Dennis on 28.08.2017.
 */

public class LiveTrackRecognitionListener {
    public LiveTrackRecognitionListener(){

    }

    public void start(){
        try{
            EventBus.getDefault().register(this);
        }catch(EventBusException e){
            e.printStackTrace();
        }

    }

    // This method will be called when a new SensorDataEvent arrived
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(ClusterMessage event) {
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Track", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean onTheWay = prefs.getBoolean("onTheWay",false);
        long startTime = prefs.getLong("startTime", 0L);

        if(event.state == ClusterMessage.STATE.LEAVING && !onTheWay){
            editor.putLong("startTime",new Date().getTime());
            editor.putBoolean("onTheWay",true);
        }else if(event.state == ClusterMessage.STATE.ENTERING && onTheWay){
            long endTime = new Date().getTime();
            TrackObject to = new TrackObject(startTime,endTime);
            EventBus.getDefault().post(new SensorDataEvent(to));
            editor.putLong("startTime",0L);
            editor.putBoolean("onTheWay",false);
        }
        editor.apply();
    }

    public void stop() {
        EventBus.getDefault().unregister(this);
    }
}
