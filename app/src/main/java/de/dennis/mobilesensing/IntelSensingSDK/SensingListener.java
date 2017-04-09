package de.dennis.mobilesensing.IntelSensingSDK;

import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.sensing.SensingEvent;
import com.intel.context.sensing.SensingStatusListener;

/**
 * Created by Dennis on 28.02.2017.
 */
public class SensingListener implements SensingStatusListener {
    private final String TAG = SensingListener.class.getName();
    public SensingListener() {}
    public void onEvent(SensingEvent event) {
        Log.i(TAG, "Event: " + event.getDescription());
    }
    public void onFail(ContextError error) {
        Log.e(TAG, "Context Sensing error: " + error.getMessage());
    }
}
