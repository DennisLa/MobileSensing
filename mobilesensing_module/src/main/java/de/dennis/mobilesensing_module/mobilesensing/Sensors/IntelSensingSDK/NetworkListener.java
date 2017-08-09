package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.Network;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;

/**
 * Created by Dennis on 06.03.2017.
 */
public class NetworkListener implements com.intel.context.sensing.ContextTypeListener {
    private final String LOG_TAG = NetworkListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof Network) {
            Log.d(LOG_TAG, "Received value: " + ((Network) state).getNetworkType().toString());
            SharedPreferences prefssettings = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorsettings = prefssettings.edit();
            if(((Network) state).getNetworkType().toString().equals("WIFI")){
                editorsettings.putBoolean("isWiFi",true);
            }else{
                editorsettings.putBoolean("isWiFi",false);
            }
            editorsettings.apply();
            SharedPreferences prefsdata = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            String networkType = ((Network) state).getNetworkType().name();
            //Call when value changed
            if(!prefsdata.getString("Network","").equals(networkType))
            {
                //new Timeseries *******************************************************************
                    //Init SensorInfo
                    SensorInfo si = new SensorInfo("Network","IntelSensing Network Sensor");
                        //Add  one ValueInfo for each measure
                        si.addValueInfo(new ValueInfo("Network type","Name of the Network type e.g. WiFi","String"));
                    //Init SensorValue
                    SensorValue sv = new SensorValue(563457754321L);
                        //Add one StringEntitiy for each measure (same order)
                        sv.addStringEntity(new StringEntity(((Network) state).getNetworkType().name()));
                    //Init Time Series
                    //TODO Type, UUID, User
                    SensorTimeseries st = new SensorTimeseries(563457754321L,"Type","UUID","User",si,sv);
                //Send Event
                EventBus.getDefault().post(new SensorDataEvent(st));
                //**********************************************************************************
                SharedPreferences.Editor editor = prefsdata.edit();
                editor.putString("Network",networkType);
                editor.apply();
            }
        } else {
            Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
        }
    }
    public void onError(ContextError error) {
        Log.e(LOG_TAG, "Error: " + error.getMessage());
    }
}
