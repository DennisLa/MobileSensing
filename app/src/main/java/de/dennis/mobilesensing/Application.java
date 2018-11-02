package de.dennis.mobilesensing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.Map;

import de.dennis.mobilesensing.Uploader.LiveUpload;
import de.dennis.mobilesensing.UI.MissingPermissionListener;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;
import de.dennis.mobilesensing_module.mobilesensing.Upload.UploadManager;

/**
 * Created by Dennis on 28.02.2017.
 */
public class Application extends MultiDexApplication {
    private static Context context;
    private static SensingManager sensMang;
    private static UploadManager uplMang;
    private static LiveUpload liveUploader;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MissingPermissionListener mpl = new MissingPermissionListener();
        SharedPreferences prefs = context.getSharedPreferences("Settings",MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
        Module.init(context, false);
        sensMang = Module.getSensingManager();
        sensMang.setSensingSetting(SensingManager.SensorNames.Activity,false);
        /*sensMang.setSensingSetting(SensingManager.SensorNames.GPS,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.WLANUpload,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.ScreenOn,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Apps,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Network,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Track,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Cluster,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.GActivity,true);*/
        uplMang = Module.getUploadManager();
        startSensing();
    }
    public static Context getContext() {
        return context;
    }
    public static SensingManager getSensingManager(){
        return sensMang;
    }
    public static void startSensing(){

                sensMang.startSensing();
                uplMang.setDailyUpload(context);
                liveUploader = new LiveUpload();
    }
}
