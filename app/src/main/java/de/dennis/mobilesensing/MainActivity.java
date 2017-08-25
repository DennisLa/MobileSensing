package de.dennis.mobilesensing;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.DataAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.DoubleEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GeoPointEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.IntegerEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;


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
        this.checkPermissions();
        setContentView(R.layout.activity_main);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Module.init(getApplicationContext());
                ValueInfo vi = new ValueInfo("Name","Description","unit");
                List<ValueInfo> lvi = new ArrayList<ValueInfo>();
                lvi.add(vi);
                SensorInfo si = new SensorInfo("SensorName","Description",lvi);
                //

                SensorValue sv = new SensorValue(9277456789L);
                sv.addStringEntitiy(new StringEntity("hello"));
                sv.addGeoPointEntity(new GeoPointEntity(4.5,4.6));
                sv.addIntegerEntitiy(new IntegerEntity(1));
                sv.addDoubleEntitiy(new DoubleEntity(2.3345));

                List<SensorValue> lsv = new ArrayList<SensorValue>();
                lsv.add(sv);
                //
                SensorTimeseries st = new SensorTimeseries(963457754321L,"Type","ID","User",si,lsv);
                //
                DataAdapter da = new DataAdapter();
                //da.deleteAllTimeseries();
                List<SensorTimeseries> p = da.getAllSensorTimeseries();
                da.saveTimeseriesToOB(st);
                Log.d("MainActivity","Send SensorTimeseries");
                List<SensorTimeseries> l = da.getAllSensorTimeseries();
                List<SensorValue> lgp = da.getAllSensorValues();
                for(SensorTimeseries s: l){
                    List<SensorValue> l1 = s.getValues();
                    SensorInfo s1 = s.getSensor_info();
                    try {
                        if(s.getTimestamp()==963457754321L){
                            JSONObject jo = s.toJSON();
                            String joString = jo.toString();
                            Log.d("","");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    /*
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
    */

    @Override
    protected void onPause() {
        super.onPause();
        finish();
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
