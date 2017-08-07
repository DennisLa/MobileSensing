package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.Call;

/**
 * Created by Dennis on 11.03.2017.
 */
public class CallListener implements com.intel.context.sensing.ContextTypeListener {
    private final String LOG_TAG = CallListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof Call) {
            Log.d(LOG_TAG, "Received value: " + ((Call) state).getNotificationType().name());
            //TODO StorageHelper.openDBConnection().save2CallHistory((Call) state);
        } else {
            Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
        }
    }
    public void onError(ContextError error) {
        Log.e(LOG_TAG, "Error: " + error.getMessage());
    }
}
