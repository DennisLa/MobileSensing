package de.dennis.mobilesensing.UploadService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Date;

import de.dennis.mobilesensing.Application;

/**
 * Created by Dennis on 07.04.2017.
 */
public class UploadListener extends BroadcastReceiver {
    private String TAG = UploadListener.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        //Run Upload if last Upload >24h, lastTrack <24h, and WiFi on (in case of Network Tracking)
        // &&( prefs.getBoolean("isWiFi",false)||prefs.getBoolean("Network",false) ==false)&& prefs.getLong("lastTrackServiceExecution",0L)-(new Date()).getTime() < 24 * 3600000)
        long now = (new Date()).getTime();
        long lastTime = prefs.getLong("lastTimeUploadServiceExecution",0L);
        if(now - lastTime >= 24 * 3600000) {
            SharedPreferences.Editor editor = prefs.edit();
            if(prefs.getBoolean("WLANUpload",false) && prefs.getBoolean("isWiFi",false))
            {
                //TODO editor.putLong("lastTimeUploadServiceExecution",BaasBoxUploader.startUpload(prefs.getLong("lastTimeUploadServiceExecution",0L),now));
            }else{
               //TODO editor.putLong("lastTimeUploadServiceExecution",BaasBoxUploader.startUpload(prefs.getLong("lastTimeUploadServiceExecution",0L),now));
            }
            editor.apply();
        }
    }
}
