package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.PowerConnectionReceiver;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;

public class ChatbotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        Log.d("CHATBOT","onCreate");
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            String chatID = uri.getQueryParameter("chatID");
            String gpsNeeded = uri.getQueryParameter("GPS");
            String screenNeeded = uri.getQueryParameter("Screen");
            String appNeeded = uri.getQueryParameter("App");
            String networkNeeded = uri.getQueryParameter("Network");
            String trackNeeded = uri.getQueryParameter("Track");
            String activityNeeded = uri.getQueryParameter("Activity");
            SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if(chatID != null && !chatID.equals("")){
                editor.putString("chatID",chatID);
            }
            if(gpsNeeded != null && !gpsNeeded.equals("")){
                if(gpsNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.GPS.name(),true);
                }else if(gpsNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.GPS.name(),false);
                }
            }
            if(screenNeeded != null && !screenNeeded.equals("")){
                if(screenNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.ScreenOn.name(),true);
                }else if(screenNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.ScreenOn.name(),false);
                }
            }
            if(appNeeded != null && !appNeeded.equals("")){
                if(appNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.Apps.name(),true);
                }else if(appNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.Apps.name(),false);
                }
            }
            if(networkNeeded != null && !networkNeeded.equals("")){
                if(networkNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.Network.name(),true);
                }else if(networkNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.Network.name(),false);
                }
            }
            if(trackNeeded != null && !trackNeeded.equals("")){
                if(trackNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.Track.name(),true);
                    editor.putBoolean(SensingManager.SensorNames.Cluster.name(),true);
                }else if(trackNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.Track.name(),false);
                    editor.putBoolean(SensingManager.SensorNames.Cluster.name(),false);
                }
            }
            if(activityNeeded != null && !activityNeeded.equals("")){
                if(activityNeeded.equals("1")){
                    editor.putBoolean(SensingManager.SensorNames.GActivity.name(),true);
                }else if(gpsNeeded.equals("0")){
                    editor.putBoolean(SensingManager.SensorNames.GActivity.name(),false);
                }
            }
            editor.apply();
        }
        this.checkPermissions();
    }

    public void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.BATTERY_STATS,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
            boolean flag = false;
            for (int i = 0; i < permissions.length; i++) {
                if (checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                requestPermissions(permissions, 1);
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CHATBOT","onStart");
        checkPermissions();
        Application.startSensing();
        finish();
    }
}
