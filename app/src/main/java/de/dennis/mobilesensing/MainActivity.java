package de.dennis.mobilesensing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Date;
import java.util.List;

import de.dennis.mobilesensing.UploadService.BaasBoxUploader;
import de.dennis.mobilesensing.UploadService.UploadListener;
import de.dennis.mobilesensing.storage.StorageHelper;
import de.dennis.mobilesensing.storage.Wrapper.wLocation;

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
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BaasBoxUploader.startUpload(0);
                /*SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("lastTimeUploadServiceExecution",0L);
                editor.apply();
                Intent i = new Intent(Application.getContext(), UploadListener.class);
                sendBroadcast(i);*/
                List<wLocation> myList =  StorageHelper.openDBConnection().getAllHistoryLocs(0, 1593452902194L, false);
                Log.d("TEST", myList.get(0).getTimestamp()+"");
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
        if(Session == null)
        {
            Intent i = new Intent(Application.getContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
