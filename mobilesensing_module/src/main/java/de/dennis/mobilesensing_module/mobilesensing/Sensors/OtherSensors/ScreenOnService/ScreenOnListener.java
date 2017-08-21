package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;

/**
 * Created by Dennis on 04.05.2017.
 */
public class ScreenOnListener extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        boolean isScreenOn= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();

        SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        Date d= new Date();
        if(!prefs.getBoolean("ScreenOn",false)==isScreenOn)
        {
            Log.d("SCREENON_Listener","Screen on: "+isScreenOn);
            //TODO StorageHelper.openDBConnection().save2ScreenOnHistory(new ScreenOn(d.getTime(),isScreenOn));
            SharedPreferences.Editor editor = prefs.edit();

            //new Timeseries *******************************************************************
            //Init SensorInfo
            SensorInfo si = new SensorInfo("Screen Status","System");
            //Add  one ValueInfo for each measure
            si.addValueInfo(new ValueInfo("Screen Status","Screen Status: true or false","String"));
            //Init SensorValue
            Long tsLong = System.currentTimeMillis();
            SensorValue sv = new SensorValue(tsLong);
            //Add one StringEntitiy for each measure (same order)
            String isScreenOnString = String.valueOf(isScreenOn);
            sv.addStringEntity(new StringEntity(isScreenOnString));
            //Init Time Series
            //TODO Type, UUID, User
            SensorTimeseries st = new SensorTimeseries(tsLong,"Type","UUID","User",si,sv);
            //Send Event
            EventBus.getDefault().post(new SensorDataEvent(st));
            //**********************************************************************************
            editor.putBoolean("ScreenOn",isScreenOn);
            editor.apply();
            Log.d("ObjectBox_ScreenOn","updated to"+isScreenOn);
            editor.apply();
        }
    }
}
