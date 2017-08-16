package de.dennis.mobilesensing_module.mobilesensing.SensingManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.intel.context.Sensing;
import com.intel.context.error.ContextError;
import com.intel.context.exception.ContextProviderException;
import com.intel.context.item.ContextType;
import com.intel.context.option.ContinuousFlag;
import com.intel.context.option.activity.ActivityOptionBuilder;
import com.intel.context.option.activity.Mode;
import com.intel.context.option.activity.ReportType;
import com.intel.context.option.deviceposition.DevicePositionOptionBuilder;
import com.intel.context.sensing.ContextTypeListener;
import com.intel.context.sensing.InitCallback;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.ActivityListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.CallListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.DevicePositionListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.LocationListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.NetworkListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.SensingListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplicationService;

/**
 * Created by Dennis on 12.03.2017.
 */
public class SensingManager {
    //Intel Sensing SDK
    private Sensing mSensing;
    private ContextTypeListener mLocationListener;
    private ContextTypeListener mActivityListener;
    private ContextTypeListener mDevicePositionListener;
    private ContextTypeListener mNetworkListener;
    private ContextTypeListener mCallListener;
    //
    private RunningApplicationService mRunningAppService;
    //
    private String TAG = SensingManager.class.getName();
    //
    private SharedPreferences prefs;

    public SensingManager() {
         prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //Init Intel SDK
        mSensing = new Sensing(Module.getContext(), new SensingListener());
        //
        mSensing.start(new InitCallback() {
            public void onSuccess() {
                Log.d("APPLICATION", "Context Sensing Daemon Started");
                //
                try {
                    //Location Listener
                    mLocationListener = new LocationListener();
                    mSensing.addContextTypeListener(ContextType.LOCATION, mLocationListener);
                    //Activity Listener
                    mActivityListener = new ActivityListener();
                    mSensing.addContextTypeListener(ContextType.ACTIVITY_RECOGNITION, mActivityListener);
                    //Device Position Listener
                    mDevicePositionListener = new DevicePositionListener();
                    mSensing.addContextTypeListener(ContextType.DEVICE_POSITION, mDevicePositionListener);
                    //Network Listener
                    mNetworkListener = new NetworkListener();
                    mSensing.addContextTypeListener(ContextType.NETWORK, mNetworkListener);
                    //RunningApp Listener
                    mRunningAppService = new RunningApplicationService();
                    //Call Listener
                    mCallListener = new CallListener();
                    mSensing.addContextTypeListener(ContextType.CALL, mCallListener);
                    Log.d("APPLICATION", "Sensing started");
                } catch (ContextProviderException e) {
                    e.printStackTrace();
                }
            }
            public void onError(ContextError error) {
                Log.d("APPLICATION", "Error: " + error.getMessage());
            }
        });
    }
    public void startSensing()
    {
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        try {
            Bundle settings;
            //enable Location Sensing
            if(prefs.getBoolean("GPS",false))
            {
                mSensing.enableSensing(ContextType.LOCATION, null);
                Log.d(TAG,"GPS-Tracking enabled");
            }else{
                mSensing.disableSensing(ContextType.LOCATION);
                Log.d(TAG, "GPS-Tracking disabled");
            }
            //enable Activity Sensing
            if(prefs.getBoolean("Activity",false)){
                ActivityOptionBuilder actBui;
                actBui = new ActivityOptionBuilder();
                actBui
                        .setMode(Mode.POWER_SAVING)
                        .setReportType(ReportType.FREQUENCY)
                        .setSensorHubContinuousFlag(ContinuousFlag.NOPAUSE_ON_SLEEP);
                settings = actBui.toBundle();
                mSensing.enableSensing(ContextType.ACTIVITY_RECOGNITION, settings);
                Log.d(TAG, "Activity-Tracking enabled");
            }else{
                mSensing.disableSensing(ContextType.ACTIVITY_RECOGNITION);
                Log.d(TAG, "Activity-Tracking disabled");
            }
            //enable Device Position
            if(prefs.getBoolean("DevicePosition",false)){
                DevicePositionOptionBuilder optBui;
                optBui = new DevicePositionOptionBuilder();
                optBui.setSensorHubContinuousFlag(ContinuousFlag.NOPAUSE_ON_SLEEP);
                settings = optBui.toBundle();
                mSensing.enableSensing(ContextType.DEVICE_POSITION, settings);
                Log.d(TAG, "DevicePosition-Tracking enabled");
            }else{
                mSensing.disableSensing(ContextType.DEVICE_POSITION);
                Log.d(TAG, "DevicePosition-Tracking disabled");
            }
            //enable Network Type
            if(prefs.getBoolean("Network",false)){
                settings = new Bundle();
                settings.putLong("TIME_WINDOW", 3*1000);
                mSensing.enableSensing(ContextType.NETWORK, settings);
                Log.d(TAG, "Network-Tracking enabled");
            }else{
                mSensing.disableSensing(ContextType.NETWORK);
                Log.d(TAG, "Network-Tracking disabled");
            }
            //enable Running Applications
            if(prefs.getBoolean("Apps",false)){
                mRunningAppService.startSensingRunningApps(Module.getContext(), 20 * 1000);
                Log.d(TAG, "App-Tracking enabled");
            }else{
                mRunningAppService.stopSensingRunningApplications();
                Log.d(TAG, "App-Tracking disabled");
            }
            //enable Call
            if(prefs.getBoolean("Call",false)){
                mSensing.enableSensing(ContextType.CALL, null);
                Log.d(TAG, "Call-EarTouch-Tracking enabled");
            }else{
                mSensing.disableSensing(ContextType.CALL);
                Log.d(TAG, "Call-EarTouch-Tracking disabled");
            }
        } catch (ContextProviderException e) {
            Log.e("APPLICATION", "Error enabling context type" + e.getMessage());
        }
    }
    //Settings Interface
    public void setSensingSetting(SensorNames name, boolean run){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name.toString(),run);
        editor.apply();
    }
}
