package de.dennis.mobilesensing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.parse.Parse;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing.Notification.NotificationListener;
import de.dennis.mobilesensing.UI.MissingPermissionListener;
import de.dennis.mobilesensing.Uploader.ParseUploader;
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
    private static ParseUploader uploader;
    private static NotificationListener notificationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MissingPermissionListener mpl = new MissingPermissionListener();
        Parse.initialize(new Parse.Configuration.Builder(this)
                /*.applicationId("SensingApp")
                .server("http://141.99.12.45:1337/parse/")
                .build()*/
                .applicationId("1234567890")
                .server("http://transport.wineme.fb5.uni-siegen.de/parse")
                .build()
        );
        Module.init(context, "USERNAME");
        sensMang = Module.getSensingManager();
        sensMang.setSensingSetting(SensingManager.SensorNames.Activity,false);
        sensMang.setSensingSetting(SensingManager.SensorNames.GPS,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.WLANUpload,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.ScreenOn,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Apps,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Network,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Track,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.Cluster,true);
        sensMang.setSensingSetting(SensingManager.SensorNames.GActivity,true);
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
        if(ParseUser.getCurrentUser() != null){
            if(ParseUser.getCurrentUser().isAuthenticated()){
                sensMang.startSensing();
                uplMang.setDailyUpload(context);
                uploader = new ParseUploader();
                notificationListener = new NotificationListener();
            }
        }
    }
}
