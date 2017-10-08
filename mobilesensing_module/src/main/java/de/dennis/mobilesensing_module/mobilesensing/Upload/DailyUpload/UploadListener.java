package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.estimote.sdk.utils.L;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ActivityListener.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocationListener.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.NetworkListener.NetworkTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplicationListener.RunningApplicationObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplicationListener.RunningApplicationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOnListener.ScreenOnTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.TrackListener.TrackTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;

/**
 * Created by Dennis on 07.04.2017.
 */
public class UploadListener extends BroadcastReceiver {
    private String TAG = UploadListener.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        //Run Upload if last Upload >24h, lastTrack <24h, and WiFi on (in case of Network Tracking)
        // &&( prefs.getBoolean("isWiFi",false)||prefs.getBoolean("Network",false) ==false)&& prefs.getLong("lastTrackServiceExecution",0L)-(new Date()).getTime() < 24 * 3600000)
        long now = (new Date()).getTime();
        long lastTime = prefs.getLong("lastTimeUploadServiceExecution",0L);
        boolean upload = false;
        SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getBoolean("WLANUpload",false) && prefs.getBoolean("isWiFi",false))
        {
            upload = true;
        }else if(!prefs.getBoolean("WLANUpload",false)){
            upload = true;
        }
        if(upload){
            ObjectBoxAdapter oba = new ObjectBoxAdapter();
            List<ActivityTimeseries> activityTimeseries = oba.getActivityTimeseriesNonUpdated();
            for(ActivityTimeseries act: activityTimeseries){
                EventBus.getDefault().post(new UploadEvent(act));
            }
            List<GLocationTimeseries> gLocationTimeseries = oba.getGLocationTimeseriesNonUpdated();
            for(GLocationTimeseries glt: gLocationTimeseries){
                EventBus.getDefault().post(new UploadEvent(glt));
            }
            List<NetworkTimeseries> networkTimeseries = oba.getNetworkTimeseriesNonUpdated();
            for(NetworkTimeseries net: networkTimeseries){
                EventBus.getDefault().post(new UploadEvent(net));
            }
            List<RunningApplicationTimeseries> runningApplicationTimeseries = oba.getRunningApplicationTimeseriesNonUpdated();
            for(RunningApplicationTimeseries rap: runningApplicationTimeseries){
                EventBus.getDefault().post(new UploadEvent(rap));
            }
            List<ScreenOnTimeseries> screenOnTimeseries = oba.getScreenOnTimeseriesNonUpdated();
            for(ScreenOnTimeseries scr: screenOnTimeseries){
                EventBus.getDefault().post(new UploadEvent(scr));
            }
            List<TrackTimeseries> trackTimeseries = oba.getTrackTimeseriesNonUpdated();
            for(TrackTimeseries track: trackTimeseries){
                EventBus.getDefault().post(new UploadEvent(track));
            }
        }
    }
}
