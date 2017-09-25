package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.baasbox.android.BaasUser;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.DataAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;

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

        SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getBoolean("WLANUpload",false) && prefs.getBoolean("isWiFi",false))
        {
            DataAdapter da = new DataAdapter();
            GregorianCalendar g = new GregorianCalendar();
            g.setTimeInMillis(now);
            //2016-05-23T16:00:00.000Z
            String timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+g.get(GregorianCalendar.MONTH)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
            List<SensorTimeseries> lst = da.getAllSensorTimeseriesOlder(timestamp_day);
            for(SensorTimeseries st: lst){
                EventBus.getDefault().post(new UploadEvent(st));
            }
        }else if(!prefs.getBoolean("WLANUpload",false)){

        }
    }
}
