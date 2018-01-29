package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload.UploadListener;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    ImageButton btnSettings;
    Button btnLogout;
    Button btnTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.checkPermissions();
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectBoxAdapter oba = new ObjectBoxAdapter();
                List<ActivityTimeseries> activityTimeseries = oba.getActivityTimeseriesNonUpdated();
                for(ActivityTimeseries act: activityTimeseries){
                    EventBus.getDefault().post(new UploadEvent(act));
                }
            }
        });
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Application.getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Session",null);
                editor.apply();
                Intent i = new Intent(Application.getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Session = prefs.getString("Session", null);
        if(ParseUser.getCurrentUser() == null){
            Intent i = new Intent(Application.getContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }else{
            if(!ParseUser.getCurrentUser().isAuthenticated())
            {
                Intent i = new Intent(Application.getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }else{
                Application.startSensing();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void checkPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
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
}
