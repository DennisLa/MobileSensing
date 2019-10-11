package de.dennis.mobilesensing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.example.gpslogger.gpslogger.GPSApplication;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
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
public class Application extends GPSApplication {
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
//        Parse.initialize(new Parse.Configuration.Builder(this)
//                /*.applicationId("SensingApp")
//                .server("http://141.99.12.45:1337/parse/")
//                .build()*/
//                .applicationId("123456789")
//                .server("http://transport.wineme.fb5.uni-siegen.de/parse")
//                .server("https://parseapi.back4app.com")
//                .build()
//        );

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("yZaq7fXs1LeiWRIg92FOEV9J9eUSEAQismJCT0Sd")
                .clientKey("OSxhE2jTKgaG779sHHB3nRCg3BUk5Dn7Wi8ko0hn")
                .server("https://parseapi.back4app.com")
                .build()
        );

//        ParseObject gameScore = new ParseObject("GameScore");
//        gameScore.put("score", 17);
//        gameScore.put("playerName", "Saja Plott");
//        gameScore.put("cheatMode", false);
//        gameScore.saveInBackground();
        Module.init(context, "USERNAME");
        sensMang = Module.getSensingManager();
        sensMang.setSensingSetting(SensingManager.SensorNames.Activity,true);
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
//            ParseUser.getCurrentUser().logOutInBackground();
            if(ParseUser.getCurrentUser().isAuthenticated()){
                sensMang.startSensing();
                uplMang.setDailyUpload(context);
                uploader = new ParseUploader();
//                uploader.uploadToParse();
                notificationListener = new NotificationListener();
            }
        }
    }
}
