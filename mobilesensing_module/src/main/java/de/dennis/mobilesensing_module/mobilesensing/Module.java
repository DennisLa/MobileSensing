package de.dennis.mobilesensing_module.mobilesensing;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

import java.util.Date;

import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;
import de.dennis.mobilesensing_module.mobilesensing.Storage.Customer;
import de.dennis.mobilesensing_module.mobilesensing.Storage.Order;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageEventListener;
import de.dennis.mobilesensing_module.mobilesensing.Upload.UploadManager;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 10.07.2017.
 */

public class Module extends MultiDexApplication {
    private static BoxStore boxStore;
    private static SensingManager sensingManager;
    private static UploadManager uploadManager;
    private static String userName;

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static void init(Context app_context, String user_Name){
        context = app_context;
        userName = user_Name;
        // ObjectBox
        //boxStore = MyObjectBox.builder().androidContext(context).build();
        sensingManager = new SensingManager();
        uploadManager = new UploadManager();

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
    public static String getUser(){
        return userName;
    }
    public static long getLtc(){
        SharedPreferences prefs = context.getSharedPreferences("Cluster",MODE_PRIVATE);
        //SharedPreferences prefs = Module.getContext().getSharedPreferences("Cluster",MODE_PRIVATE);
        long lastTime = prefs.getLong("LastTime",0L);
        return lastTime;
    }
    public static void saveltc(long ltc){
        SharedPreferences prefs = context.getSharedPreferences("Cluster",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("LastTime",ltc);
        editor.apply();
    }
}
