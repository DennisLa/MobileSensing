package de.dennis.mobilesensing.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensingManager;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.ms.ptenabler.locationtools.ClusterService;


public class SettingsActivity extends AppCompatActivity {
    Button btnSave;
    Switch swtAll;
    Switch swtGPS;
    Switch swtActivity;
    Switch swtScreenOn;
    Switch swtApps;
    Switch swtCall;
    Switch swtNetwork;
    Switch swtWLANUpload;
    SharedPreferences prefs;
    Boolean isClickAllChange = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //
        //ObjectBoxAdapter oba = new ObjectBoxAdapter();
        //List<ClusterObject> cl = oba.getAllClusterObjects();
        //Button to save settings
        btnSave = (Button) findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(getApplicationContext(), ClusterService.class);
                getApplicationContext().startService(service);
                /*Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.GPS,swtGPS.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.Network,swtNetwork.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.Call,swtCall.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.Apps,swtApps.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.Activity,swtActivity.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.ScreenOn,swtScreenOn.isChecked());
                Application.getSensingManager().setSensingSetting(SensingManager.SensorNames.WLANUpload,swtWLANUpload.isChecked());
                Application.getSensingManager().startSensing();*/

            }
        });
        //Switches for Settings
        swtAll = (Switch) findViewById(R.id.swtAll);
        swtAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isClickAllChange) {
                    Boolean check = true;
                    if (!isChecked) {
                        check = false;
                    }
                    swtAll.setChecked(check);
                    swtGPS.setChecked(check);
                    swtActivity.setChecked(check);
                    swtScreenOn.setChecked(check);
                    swtApps.setChecked(check);
                    swtCall.setChecked(check);
                    swtNetwork.setChecked(check);
                }
            }
        });
        swtWLANUpload = (Switch) findViewById(R.id.swtWLANUpload);
        swtWLANUpload.setChecked(prefs.getBoolean("WLANUpload", true));
        swtGPS = (Switch) findViewById(R.id.swtGPS);
        swtGPS.setChecked(prefs.getBoolean("GPS", true));
        swtActivity = (Switch) findViewById(R.id.swtActivity);
        swtActivity.setChecked(prefs.getBoolean("Activity", true));
        swtScreenOn = (Switch) findViewById(R.id.swtScrOn);
        swtScreenOn.setChecked(prefs.getBoolean("ScreenOn", true));
        swtApps = (Switch) findViewById(R.id.swtApp);
        swtApps.setChecked(prefs.getBoolean("Apps", true));
        swtCall = (Switch) findViewById(R.id.swtCall);
        swtCall.setChecked(prefs.getBoolean("Call", true));
        swtNetwork = (Switch) findViewById(R.id.swtNetwork);
        swtNetwork.setChecked(prefs.getBoolean("Network", true));
        swtGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        swtActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        swtScreenOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        swtApps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        swtCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        swtNetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkIfAllChecked();
            }
        });
        checkIfAllChecked();
    }
    private void checkIfAllChecked(){
        isClickAllChange = false;
        if(swtNetwork.isChecked()&&swtGPS.isChecked()&&swtScreenOn.isChecked()&&swtActivity.isChecked()&&swtApps.isChecked()&&swtCall.isChecked()){
            swtAll.setChecked(true);
        }else{
            swtAll.setChecked(false);
        }
        isClickAllChange = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Application.getContext(), WebviewActivity.class);
        startActivity(i);
        finish();
    }
}
