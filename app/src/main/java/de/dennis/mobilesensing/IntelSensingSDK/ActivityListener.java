package de.dennis.mobilesensing.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.ActivityRecognition;
import com.intel.context.item.Item;
import com.intel.context.sensing.ContextTypeListener;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.storage.StorageHelper;

/**
 * Created by Dennis on 06.03.2017.
 */
public class ActivityListener implements ContextTypeListener {
    private final String LOG_TAG = ActivityListener.class.getName();

    public void onReceive(Item state) {
        if (state instanceof com.intel.context.item.ActivityRecognition) {
            Log.d(LOG_TAG, "Received value: " + ((ActivityRecognition) state).getMostProbableActivity().getActivity() + " " + ((ActivityRecognition) state).getMostProbableActivity().getProbability());
            SharedPreferences prefs = Application.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            String activitiyName = ((ActivityRecognition) state).getMostProbableActivity().getActivity().name();
            if(!prefs.getString("Activity","").equals(activitiyName))
            {
                StorageHelper.openDBConnection().save2ActHistory((ActivityRecognition) state);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Activity",activitiyName);
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
