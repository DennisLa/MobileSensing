package de.dennis.mobilesensing;

import android.content.Context;
import com.baasbox.android.BaasBox;

import de.dennis.mobilesensing.UploadService.UploadService;

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
        sensMang = new SensingManager();
        sensMang.startSensing();
        uploadService = new UploadService();
        uploadService.startUploadService(context,1 * 3600000); //Jede Stunde prüfen ob WLAN und letzter Upload >24h
        //BaasBox Init
        BaasBox client;
        BaasBox.Builder b = new BaasBox.Builder(context);
        client = b.setApiDomain("141.99.12.45")
                .setAppCode("1234567890")
                .init();
    }
    public static Context getContext() {
        return context;
    }
    public static SensingManager getSensingManager(){
        return sensMang;
    }
}
