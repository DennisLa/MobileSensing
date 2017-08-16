package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.baasbox.android.BaasUser;

import java.util.Date;

import de.dennis.mobilesensing_module.mobilesensing.Module;

/**
 * Created by Dennis on 07.04.2017.
 */
public class UploadListener extends BroadcastReceiver {
    private String TAG = UploadListener.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = Module.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
        //Run Upload if last Upload >24h, lastTrack <24h, and WiFi on (in case of Network Tracking)
        // &&( prefs.getBoolean("isWiFi",false)||prefs.getBoolean("Network",false) ==false)&& prefs.getLong("lastTrackServiceExecution",0L)-(new Date()).getTime() < 24 * 3600000)
        long now = (new Date()).getTime();
        long lastTime = prefs.getLong("lastTimeUploadServiceExecution",0L);

        SharedPreferences.Editor editor = prefs.edit();
        DailyUploader du = new DailyUploader();
        if(prefs.getBoolean("WLANUpload",false) && prefs.getBoolean("isWiFi",false) && !prefs.getString("UploadSession","").equals("") && !prefs.getString("UploadUrl","").equals(""))
        {
            du.startUpload( BaasUser.current().getName(),prefs.getString("UploadSession",""),prefs.getString("UploadUrl",""));
        }else if(!prefs.getBoolean("WLANUpload",false) && !prefs.getString("UploadSession","").equals("") && !prefs.getString("UploadUrl","").equals("")){
            du.startUpload(BaasUser.current().getName(),prefs.getString("UploadSession",""),prefs.getString("UploadUrl",""));
        }
    }
}
