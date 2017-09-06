package de.dennis.mobilesensing_module.mobilesensing.SensingManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

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
import de.dennis.mobilesensing_module.mobilesensing.PermissionActivity;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleLocation.GLocationListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.ActivityListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.CallListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.DevicePositionListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.LocationListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.NetworkListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.SensingListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.LiveTrackRecognition.LiveTrackRecognitionListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplicationService;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageEventListener;

import java.util.ArrayList;



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
    private GLocationListener mGLocationListener;
    private LiveTrackRecognitionListener mLiveTrackRecognitionListener;
    private boolean locationStarted=false;
    private StorageEventListener sel;
    private int firstStart = 0;

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
        mGLocationListener = new GLocationListener(Module.getContext(),60*1000,60*1000); //context,interval, fastest interval |Changed Interval to 1 Min
        initSensing(false);
        sel = new StorageEventListener();
        //

        Log.d("APPLICATION","test");
    }
    public void checkPermissions(){
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ArrayList<String> permissionList = new ArrayList<String> ();
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if (prefs.getBoolean("GPS",false)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (prefs.getBoolean("Network",false)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (prefs.getBoolean("Call",false)) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
            permissionList.add(Manifest.permission.READ_CONTACTS);
            permissionList.add(Manifest.permission.READ_CALL_LOG);
        }
        Intent intent = new Intent(Module.getContext(), PermissionActivity.class);
        intent.putStringArrayListExtra("PermissionList",permissionList);
        Context context = Module.getContext();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Module.getContext().startActivity(intent);
    }
    public void startSensing()
    {
        checkPermissions();
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        try {
            Bundle settings;
            //enable Location Sensing
            if(prefs.getBoolean("GPS",false)&&
                    ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    )
            {
                mGLocationListener.getCoordinates();
                mGLocationListener.startLocationUpdates();
               // mSensing.enableSensing(ContextType.LOCATION, null);
                Log.d(TAG,"GPS-Tracking enabled");
                Toast.makeText(Module.getContext(), "GPS Sensor activated", Toast.LENGTH_SHORT).show();
                locationStarted =true;
            }else{
                if (locationStarted) {
                    mGLocationListener.stopLocationUpdates();
                    //mSensing.disableSensing(ContextType.LOCATION);
                    Log.d(TAG, "GPS-Tracking disabled");
                }
            }
            if(mActivityListener != null && mCallListener != null || mDevicePositionListener != null || mNetworkListener !=null ){
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
                if(prefs.getBoolean("Network",false)&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        ){
                    settings = new Bundle();
                    settings.putLong("TIME_WINDOW", 3*1000);
                    mSensing.enableSensing(ContextType.NETWORK, settings);
                    Log.d(TAG, "Network-Tracking enabled");
                    Toast.makeText(Module.getContext(), "Network Sensor activated", Toast.LENGTH_SHORT).show();
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
                if(prefs.getBoolean("Call",false)&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED&&
                        ActivityCompat.checkSelfPermission(Module.getContext(),Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                        ){
                    mSensing.enableSensing(ContextType.CALL, null);
                    Toast.makeText(Module.getContext(), "Call Sensor activated", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Call-EarTouch-Tracking enabled");
                }else{
                    mSensing.disableSensing(ContextType.CALL);
                    Log.d(TAG, "Call-EarTouch-Tracking disabled");
                }
                //enable Track
                if(prefs.getBoolean("Track",false)){
                    mLiveTrackRecognitionListener = new LiveTrackRecognitionListener();
                    Log.d(TAG, "Track-Tracking enabled");
                }else{
                    mLiveTrackRecognitionListener.stop();
                    Log.d(TAG, "Track-Tracking disabled");
                }
            }else initSensing(true);
        } catch (ContextProviderException e) {
            Log.e("APPLICATION", "Error enabling context type" + e.getMessage());
        }
    }

    private void initSensing(final boolean startAgain) {
        mSensing.start(new InitCallback() {
            public void onSuccess() {
                Log.d("APPLICATION", "Context Sensing Daemon Started");
                //
                try {
                    //Location Listener
                    //mLocationListener = new LocationListener();
                    //mSensing.addContextTypeListener(ContextType.LOCATION, mLocationListener);
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
                    if(startAgain){
                        startSensing();
                    }
                } catch (ContextProviderException e) {
                    Log.d("APPLICATION", "Sensing not started");
                    e.printStackTrace();
                }
                Log.d("APPLICATION","test");
            }
            public void onError(ContextError error) {
                Log.d("APPLICATION", "Error: " + error.getMessage());
            }
        });
    }

    //Settings Interface
    public void setSensingSetting(SensorNames name, boolean run){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name.toString(),run);
        editor.apply();
    }
}
