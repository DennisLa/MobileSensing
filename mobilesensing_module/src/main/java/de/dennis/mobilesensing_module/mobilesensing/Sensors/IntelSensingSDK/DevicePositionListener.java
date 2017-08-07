package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.DevicePositionItem;
import com.intel.context.item.Item;

import de.dennis.mobilesensing_module.mobilesensing.Module;

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
