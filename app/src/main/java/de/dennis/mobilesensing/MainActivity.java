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

import com.intel.context.item.Network;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing.UploadService.BaasBoxUploader;
import de.dennis.mobilesensing.UploadService.MongoNodeUploader;
import de.dennis.mobilesensing.UploadService.UploadListener;
import de.dennis.mobilesensing_module.mobilesensing.Storage.DataAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StringEntitiy;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ValueInfo;


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
               // MongoNodeUploader.startUpload(0,1000000000);


                //Init new Timeseries
                SensorTimeseries st = new SensorTimeseries();
                // timestamp as unique identifier used fo live upload and objectbox
                long timestamp = 123456789l;
                st.setTimestamp(timestamp);
                // JSON Field timestamp_day 2016-05-23T16:00:00.000Z
                GregorianCalendar g = new GregorianCalendar();
                g.setTimeInMillis(timestamp);
                String timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+g.get(GregorianCalendar.MONTH)+"-"
                        +g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
                st.setTimestamp_day(timestamp_day);
                // Additional JSON Field / User = "" because its unknown
                String type = "sensor";
                st.setType(type);
                String sensorid = "";
                st.setSensor_id(sensorid);
                String user = "";
                st.setUser(user);
                // JSON Field Info
                SensorInfo si = new SensorInfo();
                si.setSensor_name("Nerwork");
                si.setDescription("Provides information related to network connections. " +
                        "A new item is notified when a network event occurs (for example, the network access is disconnected or connected)." +
                        " By default, every two hours, the state is refreshed with traffic and other network values that change.");
                List<ValueInfo> vi = new ArrayList<>();
                vi.add(new ValueInfo("IP","IP-Adress in current Network","IP"));
                si.setValue_info(vi);
                st.setSensor_info(si);
                // JSON Array Values
                List<SensorValue> sv = new ArrayList<>();
                List<StringEntitiy> sel = new ArrayList<>();
                sel.add(new StringEntitiy(123345678+""));
                sv.add(new SensorValue(timestamp,sel));
                st.setValues(sv);
                //
                DataAdapter da = new DataAdapter();
                da.saveTimeseriesToOB(st);
                Log.d("MainActivity","Send SensorTimeseries");
                List<SensorTimeseries> l = da.getAllSensorTimeseries();
                for(SensorTimeseries s: l){
                    Log.d("MainActivity", s.toString());
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
        /*SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Session = prefs.getString("Session", null);
        if(Session == null)
        {
            Intent i = new Intent(Application.getContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
