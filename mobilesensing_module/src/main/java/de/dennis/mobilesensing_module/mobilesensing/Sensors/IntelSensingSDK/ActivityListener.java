package de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.intel.context.error.ContextError;
import com.intel.context.item.ActivityRecognition;
import com.intel.context.item.Item;
import com.intel.context.sensing.ContextTypeListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ActivityListener.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;

/**
 * Created by Dennis on 06.03.2017.
 */
public class ActivityListener implements ContextTypeListener {
    private final String LOG_TAG = ActivityListener.class.getName();
    private String activityFinal;

    public void onReceive(Item state) {
        if (state instanceof com.intel.context.item.ActivityRecognition) {
            Log.d(LOG_TAG, "Received value: " + ((ActivityRecognition) state).getMostProbableActivity().getActivity() + " " + ((ActivityRecognition) state).getMostProbableActivity().getProbability());
            SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            String activitiyName = ((ActivityRecognition) state).getMostProbableActivity().getActivity().name();
            switch (activitiyName) {
                case "SEDENTARY":
                    int sedentary = prefs.getInt("Activity_Sedentary", 0);
                    sedentary += ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_Sedentary", sedentary);
                    break;
                case "WALKING":
                    int walking = prefs.getInt("Activity_Walking", 0);
                    walking+= ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_Walking", walking);
                    break;
                case "INCAR":
                    int incar = prefs.getInt("Activity_InCar", 0);
                    incar+= ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_InCar", incar);
                    break;
                case "BIKING":
                    int biking = prefs.getInt("Activity_Biking", 0);
                    biking+= ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_Biking", biking);
                    break;
                case "RUNNING":
                    int running = prefs.getInt("Activity_Running", 0);
                    running+= ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_Running", running);
                    break;
                case "RANDOM":
                    int random = prefs.getInt("Activity_Random", 0);
                    random+= ((ActivityRecognition) state).getMostProbableActivity().getProbability();
                    editor.putInt("Activity_Random", random);
                    break;
                default:
                    break;
            }
            int actSum = prefs.getInt("Activity_Sum",0)+1;
            editor.putInt("Activity_Sum",actSum);
            editor.apply();
            long actTime = prefs.getLong("Activity_Time",0L);
            //if difference between last time activity and now > 60seconds
            if(state.getTimestamp() - actTime >= 60*1000)
            {
                int random = prefs.getInt("Activity_Random",0);
                int running = prefs.getInt("Activity_Running", 0);
                int biking = prefs.getInt("Activity_Biking", 0);
                int incar = prefs.getInt("Activity_InCar", 0);
                int walking = prefs.getInt("Activity_Walking", 0);
                int sedentary = prefs.getInt("Activity_Sedentary", 0);
                ArrayList<Integer> list = new ArrayList<>();
                list.add(walking);
                list.add(biking);
                list.add(running);
                list.add(incar);
                list.add(random);
                list.add(sedentary);
                float max = Integer.MIN_VALUE;
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
                int prob = (random+running+walking+incar+sedentary+random)/actSum;
                Log.d("Minute_Actvity",activitiyName+" , "+ prob);
                activityFinal =activitiyName+","+ prob;
                if(!prefs.getString("Activity","").equals(activitiyName))
                {
                    //TODO
                    editor.putString("Activity", activitiyName);
                }
                editor.putLong("Activity_Time",state.getTimestamp());
                editor.putInt("Activity_Sum",0);
                editor.putInt("Activity_Walking", 0);
                editor.putInt("Activity_Biking", 0);
                editor.putInt("Activity_Running", 0);
                editor.putInt("Activity_InCar", 0);
                editor.putInt("Activity_Random", 0);
                editor.putInt("Activity_Sedentary", 0);

                //new Timeseries *******************************************************************
                ActivityObject ao = new ActivityObject(System.currentTimeMillis(),activityFinal);
                //Send Event
                EventBus.getDefault().post(new SensorDataEvent(ao));
                //**********************************************************************************
                editor.putString("Activity",activityFinal);
                Log.d("ObjectBox_Activity","updated to"+activityFinal);

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
