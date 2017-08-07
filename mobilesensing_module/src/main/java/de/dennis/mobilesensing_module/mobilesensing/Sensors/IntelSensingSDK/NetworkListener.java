package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.Network;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StringEntitiy;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ValueInfo;
import io.objectbox.relation.ToOne;

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
            if(!prefsdata.getString("Network","").equals(networkType))
            {
                //TODO StorageHelper.openDBConnection().save2NetworkHistory((Network) state);
                SharedPreferences.Editor editor = prefsdata.edit();
                editor.putString("Network",networkType);
                editor.apply();

                // timestamp as unique identifier used fo live upload and objectbox
                long timestamp = state.getTimestamp();
                // JSON Field timestamp_day
                GregorianCalendar g = new GregorianCalendar();
                g.setTimeInMillis(timestamp);
                String timestamp_day = g.toString();
                // Additional JSON Field / User = "" because its unknown
                String type = "sensor";
                String sensorid = "";
                String user = "";
                // JSON Field Info
                List<ValueInfo> vi = new ArrayList<>();
                vi.add(new ValueInfo("","",""));
                SensorInfo si = new SensorInfo("Network","Provides information related to network connections. " +
                        "A new item is notified when a network event occurs (for example, the network access is disconnected or connected)." +
                        " By default, every two hours, the state is refreshed with traffic and other network values that change.",vi);
                // JSON Array Values
                List<SensorValue> sv = new ArrayList<>();
                List<StringEntitiy> sel = new ArrayList<>();
                sel.add(new StringEntitiy(((Network) state).getIp()+""));
                sv.add(new SensorValue(state.getTimestamp(),sel));
                // Build Timeseries Object
                SensorTimeseries ts = new SensorTimeseries(state.getTimestamp(),timestamp_day,type,sensorid,user,si,sv);
                //Send Event
                EventBus.getDefault().post(new SensorDataEvent(ts));
            }
        } else {
            Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
        }
    }
    public void onError(ContextError error) {
        Log.e(LOG_TAG, "Error: " + error.getMessage());
    }
}
