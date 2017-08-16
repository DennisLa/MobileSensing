package de.dennis.mobilesensing;

import android.content.Context;

import com.baasbox.android.BaasUser;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensorNames;
import de.dennis.mobilesensing_module.mobilesensing.Upload.UploadManager;

/**
 * Created by Dennis on 28.02.2017.
 */
public class Application extends android.app.Application {
    private static Context context;
    private static SensingManager sensMang;
    private static UploadManager uplMang;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Module.init(context);
        sensMang = Module.getSensingManager();
        sensMang.setSensingSetting(SensorNames.Activity,true);
        sensMang.startSensing();
        uplMang = Module.getUploadManager();
        uplMang.setDailyUpload(context, BaasUser.current().getToken(),"http://141.99.12.45:3000/sendData");

    }
    public static Context getContext() {
        return context;
    }
    public static SensingManager getSensingManager(){
        return sensMang;
    }
}
