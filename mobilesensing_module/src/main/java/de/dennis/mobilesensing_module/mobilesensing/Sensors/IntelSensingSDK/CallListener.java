package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.Call;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;

/**
 * Created by Dennis on 11.03.2017.
 */
public class CallListener implements com.intel.context.sensing.ContextTypeListener {
    private final String LOG_TAG = CallListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof Call) {
            Log.d(LOG_TAG, "Received value: Call " + ((Call) state).getNotificationType().name());

            String callResult = "Contact Name: "+((Call) state).getContactName()+","+"Caller number: "+((Call) state).getCaller();
            Log.d(LOG_TAG, "Received value: Call " + callResult);
            SharedPreferences prefsdata = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);

            //new Timeseries *******************************************************************
            //Init SensorInfo
            SensorInfo si = new SensorInfo("Call","IntelSensing Network Sensor");
            //Add  one ValueInfo for each measure
            si.addValueInfo(new ValueInfo("Call Type","Caller Information e.g. Caller phone number","String"));
            //Init SensorValue
            Long tsLong = System.currentTimeMillis()/1000;
            SensorValue sv = new SensorValue(tsLong);
            //Add one StringEntitiy for each measure (same order)
            sv.addStringEntity(new StringEntity(callResult));
            //Init Time Series
            //TODO Type, UUID, User
            SensorTimeseries st = new SensorTimeseries(tsLong,"Type","UUID","User",si,sv);
            //Send Event
            EventBus.getDefault().post(new SensorDataEvent(st));
            //**********************************************************************************
            SharedPreferences.Editor editor = prefsdata.edit();
            editor.putString("Call",callResult);
            editor.apply();
            Log.d("ObjectBox_Call","updated to"+callResult);

            //TODO StorageHelper.openDBConnection().save2CallHistory((Call) state);
        } else {
            Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
        }
    }
    public void onError(ContextError error) {
        Log.e(LOG_TAG, "Error: " + error.getMessage());
    }
}
