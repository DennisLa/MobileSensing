package de.dennis.mobilesensing.ScreenOnService;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.util.Date;

import de.dennis.mobilesensing.RunningApplicationService.RunningApplication;
import de.dennis.mobilesensing.storage.StorageHelper;

/**
 * Created by Dennis on 04.05.2017.
 */
public class ScreenOnListener extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        boolean isScreenOn= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();

        SharedPreferences prefs = de.dennis.mobilesensing.Application.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        Date d= new Date();
        if(!prefs.getBoolean("ScreenOn",false)==isScreenOn)
        {
            Log.d("SCREENON_Listener","Screen on: "+isScreenOn);
            StorageHelper.openDBConnection().save2ScreenOnHistory(new ScreenOn(d.getTime(),isScreenOn));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("ScreenOn",isScreenOn);
            editor.apply();
        }
    }
}
