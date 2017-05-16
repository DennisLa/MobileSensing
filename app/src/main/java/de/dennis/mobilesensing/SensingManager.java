package de.dennis.mobilesensing;

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

import de.dennis.mobilesensing.IntelSensingSDK.ActivityListener;
import de.dennis.mobilesensing.IntelSensingSDK.CallListener;
import de.dennis.mobilesensing.IntelSensingSDK.DevicePositionListener;
import de.dennis.mobilesensing.IntelSensingSDK.LocationListener;
import de.dennis.mobilesensing.IntelSensingSDK.NetworkListener;
import de.dennis.mobilesensing.IntelSensingSDK.SensingListener;
import de.dennis.mobilesensing.RunningApplicationService.RunningApplicationService;
import de.dennis.mobilesensing.ScreenOnService.ScreenOnService;
import de.dennis.mobilesensing.TrackService.TrackService;

/**
 * Created by Dennis on 12.03.2017.
 */
public class SensingManager {
    //Intel Sensing SDK
    private Sensing mSensing;
    private ContextTypeListener mLocationListener;
    private ContextTypeListener mActivityListener;
    private ContextTypeListener mNetworkListener;
    private ContextTypeListener mCallListener;
    //
    private RunningApplicationService mRunningAppService;
    private TrackService mTrackService;
    private ScreenOnService mScreenOnService;
    //
    private boolean isLocationOn;
    private boolean isActivityOn;
    private boolean isScreenOn;
    private boolean isNetworkOn;
    private boolean isRunAppOn;
    private boolean isCallOn;
    //
    private String TAG = SensingManager.class.getName();

    public SensingManager() {
    }
    public void startSensing(){
        mSensing = new Sensing(Application.getContext(), new SensingListener());
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
                    //ScreenOn Listener
                    mScreenOnService = new ScreenOnService();
                    //Network Listener
                    mNetworkListener = new NetworkListener();
                    mSensing.addContextTypeListener(ContextType.NETWORK, mNetworkListener);
                    //RunningApp Listener
                    mRunningAppService = new RunningApplicationService();
                    //Call Listener
                    mCallListener = new CallListener();
                    mSensing.addContextTypeListener(ContextType.CALL, mCallListener);
                    //Track Listener
                    mTrackService = new TrackService();
                    Log.d("APPLICATION", "Sensing started");
                } catch (ContextProviderException e) {
                    e.printStackTrace();
                }
                loadSensingSettings();
            }
            public void onError(ContextError error) {
                Log.d("APPLICATION", "Error: " + error.getMessage());
            }
        });
    }
    public void loadSensingSettings()
    {
        SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Log.d(TAG, (prefs.getBoolean("GPS",true))+" GPS VALUE");
        try {
            Bundle settings;
            //enable Location Sensing
            if(prefs.getBoolean("GPS",true) && !isLocationOn)
            {
                mSensing.enableSensing(ContextType.LOCATION, null);
                isLocationOn=true;
                Log.d(TAG,"GPS-Tracking enabled");
            }else if(!prefs.getBoolean("GPS",true) && isLocationOn){
                mSensing.disableSensing(ContextType.LOCATION);
                isLocationOn =false;
                Log.d(TAG, "GPS-Tracking disabled");
            }
            //enable Activity Sensing
            if(prefs.getBoolean("Activity",true) && !isActivityOn){
                ActivityOptionBuilder actBui;
                actBui = new ActivityOptionBuilder();
                actBui
                        .setMode(Mode.POWER_SAVING)
                        .setReportType(ReportType.FREQUENCY)
                        .setSensorHubContinuousFlag(ContinuousFlag.NOPAUSE_ON_SLEEP);
                settings = actBui.toBundle();
                mSensing.enableSensing(ContextType.ACTIVITY_RECOGNITION, settings);
                isActivityOn = true;
                Log.d(TAG, "Activity-Tracking enabled");
            }else if(!prefs.getBoolean("Activity",true) && isActivityOn){
                mSensing.disableSensing(ContextType.ACTIVITY_RECOGNITION);
                isActivityOn=false;
                Log.d(TAG, "Activity-Tracking disabled");
            }
            //enable ScreenOn
            if(prefs.getBoolean("ScreenOn",true) && !isScreenOn){
                mScreenOnService.startSensingScreenStatus(Application.getContext(),10*1000);
                isScreenOn=true;
                Log.d(TAG, "ScreenOn-Tracking enabled");
            }else if(!prefs.getBoolean("ScreenOn",true) && isScreenOn){
                mScreenOnService.stopSensingScreenStatus();
                isScreenOn=false;
                Log.d(TAG, "ScreenOn-Tracking disabled");
            }
            //enable Network Type
            if(prefs.getBoolean("Network",true) && !isNetworkOn){
                settings = new Bundle();
                settings.putLong("TIME_WINDOW", 60*1000);
                mSensing.enableSensing(ContextType.NETWORK, settings);
                isNetworkOn = true;
                Log.d(TAG, "Network-Tracking enabled");
            }else if (!prefs.getBoolean("Network",true) && isNetworkOn){
                mSensing.disableSensing(ContextType.NETWORK);
                isNetworkOn = false;
                Log.d(TAG, "Network-Tracking disabled");
            }
            //enable Running Applications
            if(prefs.getBoolean("Apps",true)&& !isRunAppOn){
                mRunningAppService.startSensingRunningApps(Application.getContext(), 10 * 1000);
                isRunAppOn = true;
                Log.d(TAG, "App-Tracking enabled");
            }else if(!prefs.getBoolean("Apps",true) && isRunAppOn){
                mRunningAppService.stopSensingRunningApplications();
                isRunAppOn = false;
                Log.d(TAG, "App-Tracking disabled");
            }
            //enable Call
            if(prefs.getBoolean("Call",true) && !isCallOn){
                mSensing.enableSensing(ContextType.CALL, null);
                isCallOn=true;
                Log.d(TAG, "Call-EarTouch-Tracking enabled");
            }else if(!prefs.getBoolean("Call",true) && isCallOn){
                mSensing.disableSensing(ContextType.CALL);
                isCallOn = false;
                Log.d(TAG, "Call-EarTouch-Tracking disabled");
            }
        } catch (ContextProviderException e) {
            Log.e("APPLICATION", "Error enabling context type" + e.getMessage());
        }
    }
}
