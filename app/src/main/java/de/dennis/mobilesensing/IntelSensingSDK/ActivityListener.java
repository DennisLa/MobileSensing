package de.dennis.mobilesensing.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.ActivityRecognition;
import com.intel.context.item.Item;
import com.intel.context.sensing.ContextTypeListener;

import java.util.ArrayList;
import java.util.Date;

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
            SharedPreferences.Editor editor = prefs.edit();
            String activitiyName = ((ActivityRecognition) state).getMostProbableActivity().getActivity().name();
            int walking = prefs.getInt("Activity_Walking",0);
            int biking = prefs.getInt("Activity_Biking",0);
            int running = prefs.getInt("Activity_Running",0);
            int incar = prefs.getInt("Activity_InCar",0);
            int random = prefs.getInt("Activity_Random",0);
            int sedentary = prefs.getInt("Activity_Sedentary",0);
            switch (activitiyName) {
                case "WALKING":
                    walking++;
                    editor.putInt("Activity_Walking", walking);
                    break;
                case "BIKING":
                    biking++;
                    editor.putInt("Activity_Biking", biking);
                    break;
                case "RUNNING":
                    running++;
                    editor.putInt("Activity_Running", running);
                    break;
                case "INCAR":
                    incar++;
                    editor.putInt("Activity_InCar", incar);
                    break;
                case "RANDOM":
                    random++;
                    editor.putInt("Activity_Random", random);
                    break;
                case "SEDENTARY":
                    sedentary++;
                    editor.putInt("Activity_Sedentary", sedentary);
                    break;
                default:
                    break;
            }
            long actTime = prefs.getLong("Activity_Time",0L);
            //if difference between last time activity and now > 50 Seconds (Value of Location Interval)
            if(state.getTimestamp() - actTime >= 60000)
            {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(walking);
                list.add(biking);
                list.add(running);
                list.add(incar);
                list.add(random);
                list.add(sedentary);
                int max = Integer.MIN_VALUE;
                for (Integer i : list){
                    max = Math.max(max, i);
                }
                activitiyName = "";
                int j = 0;
                if(max == walking) {
                    activitiyName = "WALKING";
                    j++;
                }
                if(max == running) {
                    activitiyName = "RUNNING";
                    j++;
                }
                if(max == biking ){
                    activitiyName = "BIKING";
                    j++;
                }
                if(max == incar){
                    activitiyName = "INCAR";
                    j++;
                }
                if(max == sedentary){
                    activitiyName = "SEDENTARY";
                    j++;
                }
                if (max == random || j > 1) {
                    activitiyName = "RANDOM";
                }
                Log.d("Minute_Actvity",activitiyName);
                if(!prefs.getString("Activity","").equals(activitiyName))
                {
                    StorageHelper.openDBConnection().save2ActHistory((ActivityRecognition) state);
                    editor.putString("Activity", activitiyName);
                }
                editor.putLong("Activity_Time",state.getTimestamp());
                editor.putInt("Activity_Walking", 0);
                editor.putInt("Activity_Biking", 0);
                editor.putInt("Activity_Running", 0);
                editor.putInt("Activity_InCar", 0);
                editor.putInt("Activity_Random", 0);
                editor.putInt("Activity_Sedentary", 0);
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
