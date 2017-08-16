package de.dennis.mobilesensing_module.mobilesensing;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.MyObjectBox;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageEventListener;
import de.dennis.mobilesensing_module.mobilesensing.Upload.UploadManager;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 10.07.2017.
 */

public class Module extends android.app.Application{
    private static BoxStore boxStore;
    private static SensingManager sensingManager;
    private static UploadManager uploadManager;

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static void init(Context app_context){
        context = app_context;
        // ObjectBox
        sensingManager = new SensingManager();
        uploadManager = new UploadManager();
        try{
            boxStore = MyObjectBox.builder().androidContext(getContext()).build();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Context getContext() {
        return context;
    }
    public static BoxStore getBoxStore() {
        return boxStore;
    }
    public static SensingManager getSensingManager() {
        return sensingManager;
    }
    public static UploadManager getUploadManager() {
        return uploadManager;
    }
}
