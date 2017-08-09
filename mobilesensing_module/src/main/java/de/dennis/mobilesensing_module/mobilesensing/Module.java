package de.dennis.mobilesensing_module.mobilesensing;


import android.content.Context;
import android.util.Log;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.MyObjectBox;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageEventListener;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 10.07.2017.
 */

public class Module extends android.app.Application{
    private static BoxStore boxStore;
    private static StorageEventListener sel;
    //
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Module","Module StartUp");
        context = getApplicationContext();
        // ObjectBox
        boxStore = MyObjectBox.builder().androidContext(getContext()).build();
        sel = new StorageEventListener();
    }
    public static void startUp(Context app_context){
        context = app_context;
        // ObjectBox
        sel = new StorageEventListener();
        try{
            boxStore = MyObjectBox.builder().androidContext(getContext()).build();
        }catch(Exception e){
            e.printStackTrace();
        }

        //
    }
    public static Context getContext() {
        return context;
    }
    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
