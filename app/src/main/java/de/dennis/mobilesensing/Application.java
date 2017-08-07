package de.dennis.mobilesensing;

import android.content.Context;
import com.baasbox.android.BaasBox;

import de.dennis.mobilesensing.UploadService.UploadService;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;

/**
 * Created by Dennis on 28.02.2017.
 */
public class Application extends android.app.Application {
    private static Context context;
    private static SensingManager sensMang;
    private static UploadService uploadService;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Module.startUp(context);
    }
    public static Context getContext() {
        return context;
    }
    public static SensingManager getSensingManager(){
        return sensMang;
    }
}
