package de.dennis.mobilesensing_module.mobilesensing.SensingManager;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.intel.context.sensing.ContextTypeListener;
import com.intel.context.sensing.InitCallback;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.MissingPermissionsEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleActivity.GActivityRecognition;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleLocation.GLocationListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.ActivityListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.NetworkListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.IntelSensingSDK.SensingListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ClusterService.ClusterJobService;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.LiveTrackRecognition.LiveTrackRecognitionListener;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplicationService;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService.ScreenOnService;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageEventListener;
import de.dennis.mobilesensing_module.mobilesensing.Upload.Delete.DeleteService;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.checkSelfPermission;


/**
 * Created by Dennis on 12.03.2017.
 */
public class SensingManager {
    //Intel Sensing SDK
    private Sensing mSensing;
    private ContextTypeListener mLocationListener;
    private DeleteService mDeleteService;
    private ContextTypeListener mActivityListener;
    private ContextTypeListener mDevicePositionListener;
    private ContextTypeListener mNetworkListener;
    private ContextTypeListener mCallListener;
    private GLocationListener mGLocationListener;
    private ScreenOnService mScreenOnService;
    private GActivityRecognition mGActivity;
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



    public SensingManager(boolean persist) {
        prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //Init Intel SDK
        mSensing = new Sensing(Module.getContext(), new SensingListener());
        mGLocationListener = new GLocationListener(Module.getContext(),prefs.getInt("GPSSlow",60*1000*5),prefs.getInt("GPSSlow",60*1000)); //context,interval, fastest interval |Changed Interval to 1 Min
        mLiveTrackRecognitionListener = new LiveTrackRecognitionListener();
        mDeleteService = new DeleteService();
        initSensing(false);
        if(persist){
            sel = new StorageEventListener();
        }
    }
    public void checkPermissions(){
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ArrayList<String> permissionList = new ArrayList<String> ();
        boolean openActivity = false;
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        if (prefs.getBoolean(SensorNames.GPS.name(),false)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            openActivity = true;
        }
        if (prefs.getBoolean(SensorNames.GActivity.name(),false)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (prefs.getBoolean(SensorNames.Network.name(),false)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (prefs.getBoolean(SensorNames.Call.name(),false)) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
            permissionList.add(Manifest.permission.READ_CONTACTS);
            permissionList.add(Manifest.permission.READ_CALL_LOG);
        }
        boolean flag = false;
        for (int i = 0; i < permissionList.size(); i++) {

            if (checkSelfPermission(Module.getContext(),permissionList.get(i)) == PackageManager.PERMISSION_DENIED) {
                flag = true;
                break;
            }
        }
        if(flag){
            /*Intent intent = new Intent(Module.getContext(), PermissionActivity.class);
            intent.putStringArrayListExtra("PermissionList",permissionList);
            Context context = Module.getContext();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Module.getContext().startActivity(intent);*/
            EventBus.getDefault().post(new MissingPermissionsEvent(permissionList));
        }
    }
    public void startSensing()
    {
        //TODO When new Sensor -> Add Sensor here to Start Sensor AND Add Permission Check of Sensor
        checkPermissions();
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        try {
            Bundle settings;
            //enable Google Activity Sensing
            if(prefs.getBoolean(SensorNames.GActivity.name(),false)&& checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mGActivity = new GActivityRecognition();
                mGActivity.requestActivityTransitionUpdates(Module.getContext());
                mGActivity.requestActivityUpdates(Module.getContext());
                // mSensing.enableSensing(ContextType.LOCATION, null);
                Log.d(TAG,"GActivity enabled");
            }else{
                if(mGActivity != null){
                    mGActivity.stopService();
                }
                Log.d(TAG,"GActivity disabled");
            }
            //enable Location Sensing
            if(prefs.getBoolean(SensorNames.GPS.name(),false)&& checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mGLocationListener.getCoordinates();
                mGLocationListener.startLocationUpdates();
               // mSensing.enableSensing(ContextType.LOCATION, null);
                Log.d(TAG,"GPS-Tracking enabled");
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
                if(prefs.getBoolean(SensorNames.Activity.name(),false)){
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
                //enable Network Type
                if(prefs.getBoolean(SensorNames.Network.name(),false)&&
                        checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&&
                        checkSelfPermission(Module.getContext(),Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED&&
                        checkSelfPermission(Module.getContext(),Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        ){
                    settings = new Bundle();
                    settings.putLong("TIME_WINDOW",prefs.getInt("NetworkInt",60*1000));
                    mSensing.enableSensing(ContextType.NETWORK, settings);
                    Log.d(TAG, "Network-Tracking enabled");
                }else{
                    mSensing.disableSensing(ContextType.NETWORK);
                    Log.d(TAG, "Network-Tracking disabled");
                }
                //enable Running Applications
                if(prefs.getBoolean(SensorNames.Apps.name(),false)){
                    mRunningAppService.startSensingRunningApps(Module.getContext(), prefs.getInt("AppInt",20*1000));
                    Log.d(TAG, "App-Tracking enabled");
                }else{
                    mRunningAppService.stopSensingRunningApplications();
                    Log.d(TAG, "App-Tracking disabled");
                }
                //enable ScreenOn
                if(prefs.getBoolean(SensorNames.ScreenOn.name(),false)){
                    mScreenOnService.startSensingScreenStatus(Module.getContext(),prefs.getInt("ScreenInt",20*1000));
                    Log.d(TAG, "Screen enabled");
                }else{
                    mScreenOnService.stopSensingScreenStatus();
                    Log.d(TAG, "Screen disabled");
                }
                //enable Track
                if(prefs.getBoolean(SensorNames.Track.name(),false)){
                    mLiveTrackRecognitionListener.start();
                    Log.d(TAG, "Track-Tracking enabled");
                }else{
                    mLiveTrackRecognitionListener.stop();
                    Log.d(TAG, "Track-Tracking disabled");
                }
                if(prefs.getBoolean(SensorNames.Cluster.name(),false)) {
                    //Cluster
                    ComponentName serviceComponent = new ComponentName(Module.getContext(), ClusterJobService.class);
                    JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                    //builder.setMinimumLatency(3600000); // wait at least
                    //builder.setOverrideDeadline(3600000); // maximum delay
                    builder.setMinimumLatency(600000); // wait at least 3600000
                    builder.setOverrideDeadline(600000); // maximum delay
                    JobScheduler jobScheduler = (JobScheduler) Module.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(builder.build());
                }else{
                    JobScheduler jobScheduler = (JobScheduler) Module.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
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
                    //Network Listener
                    mNetworkListener = new NetworkListener();
                    mSensing.addContextTypeListener(ContextType.NETWORK, mNetworkListener);
                    //RunningApp Listener
                    mRunningAppService = new RunningApplicationService();
                    //ScreenOn
                    mScreenOnService = new ScreenOnService();
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
    //Delete Service
    public void startDeleteService(Context context, long interval){
        if(mDeleteService.equals(null)){
            mDeleteService = new DeleteService();
        }
        mDeleteService.startDeleteService(context, interval);
    }
    public void stopDeleteService()
    {
        mDeleteService.stopDeleteService();
    }

    //Settings
    public void setSensingSetting(SensorNames name, boolean run){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name.toString(),run);
        editor.apply();
    }

    public void setAdvancedGPSSettings(int slowInterval, int fastInterval){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("GPSSlow",slowInterval);
        editor.putInt("GPSFast",fastInterval);
        editor.apply();
    }

    public void setAdvancedNetworkSettings(int interval){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("NetworkInt",interval);
        editor.apply();
    }

    public void setAdvancedRunningAppSettings(int interval){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("AppInt",interval);
        editor.apply();
    }

    public void setAdvancedScreenOnSettings(int interval){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ScreenInt",interval);
        editor.apply();
    }

    public enum SensorNames{
        //TODO When new Sensor -> Add Sensor Name Here to be Able to Start Sensor
        GPS,Network,Call,Apps,Activity,ScreenOn,WLANUpload,Track,Cluster,GActivity
    }
}
