package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.Network;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.storage.StorageHelper;

/**
 * Created by Dennis on 06.03.2017.
 */
public class NetworkListener implements com.intel.context.sensing.ContextTypeListener {
    private final String LOG_TAG = NetworkListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof Network) {
            Log.d(LOG_TAG, "Received value: " + ((Network) state).getNetworkType().toString());
            SharedPreferences prefssettings = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorsettings = prefssettings.edit();
            if(((Network) state).getNetworkType().toString().equals("WIFI")){
                editorsettings.putBoolean("isWiFi",true);
            }else{
                editorsettings.putBoolean("isWiFi",false);
            }
            editorsettings.apply();
            SharedPreferences prefsdata = Application.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            String networkType = ((Network) state).getNetworkType().name();
            if(!prefsdata.getString("Network","").equals(networkType))
            {
                StorageHelper.openDBConnection().save2NetworkHistory((Network) state);
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
