package de.dennis.mobilesensing.IntelSensingSDK;

import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.Item;
import com.intel.context.item.LocationCurrent;
import com.intel.context.sensing.ContextTypeListener;

import de.dennis.mobilesensing.storage.StorageHelper;

/**
 * Created by Dennis on 28.02.2017.
 */
public class LocationListener implements ContextTypeListener{
        private final String LOG_TAG = LocationListener.class.getName();

        public void onReceive(Item state) {
            if (state instanceof LocationCurrent) {
                // Obtain the list of recognized physical activities.
                Log.d(LOG_TAG,"Received value: "+((LocationCurrent) state).getLocation().toString()+", "+state.getTimestamp());
                StorageHelper.openDBConnection().save2LocHistory((LocationCurrent)state);
            } else {
                Log.d(LOG_TAG, "Invalid state type: " + state.getContextType());
            }
        }

        public void onError(ContextError error) {
            Log.e(LOG_TAG, "Error: " + error.getMessage());
        }
}
