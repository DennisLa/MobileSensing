package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.DevicePositionItem;
import com.intel.context.item.Item;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;

/**
 * Created by Dennis on 06.03.2017.
 */
public class DevicePositionListener implements com.intel.context.sensing.ContextTypeListener {
    private final String LOG_TAG = DevicePositionListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof DevicePositionItem) {
            Log.d(LOG_TAG, "Received value: " + ((DevicePositionItem) state).getType().name());
            SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            String devicePosition = ((DevicePositionItem) state).getType().name();
            SharedPreferences.Editor editor = prefs.edit();
            if(!prefs.getString("DevicePosition","").equals(devicePosition) && !devicePosition.equals("UNKNOWN"))
            {
                //TODO StorageHelper.openDBConnection().save2DevicePositionHistory((DevicePositionItem) state);
                editor.putString("DevicePosition", devicePosition);

                //new Timeseries *******************************************************************
                //Init SensorInfo
                SensorInfo si = new SensorInfo("DevicePosition","IntelSensing Network Sensor");
                //Add  one ValueInfo for each measure
                si.addValueInfo(new ValueInfo("DevicePosition Type","Position of Device e.g. in Hand","String"));
                //Init SensorValue
                Long tsLong = System.currentTimeMillis();
                SensorValue sv = new SensorValue(tsLong);
                //Add one StringEntitiy for each measure (same order)
                //sv.addStringEntity(new ObjectEntity(devicePosition));
                //Init Time Series
                //TODO Type, UUID, User
                SensorTimeseries st = new SensorTimeseries(tsLong,"Type","UUID","User",si,sv);
                //Send Event
                EventBus.getDefault().post(new SensorDataEvent(st));
                //**********************************************************************************
                editor.putString("DevicePosition",devicePosition);
                Log.d("ObjectBox_DevicePo","updated to"+devicePosition);
            }
            editor.apply();
        } else {
            Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
        }
    }
    public void onError(ContextError error) {
        Log.e(LOG_TAG, "Error: " + error.getMessage());
    }
}
