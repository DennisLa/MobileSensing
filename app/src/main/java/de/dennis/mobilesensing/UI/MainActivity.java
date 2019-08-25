package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network.NetworkTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication.RunningApplicationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOn.ScreenOnTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import io.objectbox.Box;

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
//        if (ParseUser.getCurrentUser() != null)
//            ParseUser.getCurrentUser().logOut();
        this.checkPermissions();

//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Intent i;
//                switch (item.getItemId()) {
//                    case R.id.menu_maps:
//                        i = new Intent(MainActivity.this, MapsActivity.class);
//                        MainActivity.this.startActivity(i);
//                        break;
//                    case R.id.menu_data:
//                        i = new Intent(MainActivity.this, RegisterActivity.class);
//                        MainActivity.this.startActivity(i);
//                        break;
//                    case R.id.menu_chats:
//                        i = new Intent(MainActivity.this, LoginActivity.class);
//                        MainActivity.this.startActivity(i);
//                        break;
//                }
//                return true;
//            }
//        });
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload();
//                Box timeseriesBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
//                Box objectBox = Module.getBoxStore().boxFor(GLocationsObject.class);
//                List<GLocationTimeseries>lst = timeseriesBox.getAll();
//                List<NetworkTimeseries>lst2 = timeseriesBox.getAll();
//                ObjectBoxAdapter oba = new ObjectBoxAdapter();
//                List<GLocationTimeseries> activityTimeseries = oba.getGLocationTimeseriesNonUpdated();
//                for(GLocationTimeseries act: activityTimeseries){
//                    EventBus.getDefault().post(new UploadEvent(act));
//                }
//                Intent i = new Intent(Application.getContext(), DataActivity.class);
//                startActivity(i);
//                finish();
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
//        btnLogout = (Button) findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings",MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("Session",null);
//                editor.apply();
//                Intent i = new Intent(Application.getContext(), LoginActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }

    private void Upload() {
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        List<GActivityTimeseries> gActivityTimeseries = oba.getGActivityTimeseriesNonUpdated();
        for(GActivityTimeseries act: gActivityTimeseries){
            EventBus.getDefault().post(new UploadEvent(act));
        }
        List<ActivityTimeseries> activityTimeseries = oba.getActivityTimeseriesNonUpdated();
        for(ActivityTimeseries act: activityTimeseries){
            EventBus.getDefault().post(new UploadEvent(act));
        }
        List<GLocationTimeseries> gLocationTimeseries = oba.getGLocationTimeseriesNonUpdated();
        for(GLocationTimeseries glt: gLocationTimeseries){
            EventBus.getDefault().post(new UploadEvent(glt));
        }
        List<NetworkTimeseries> networkTimeseries = oba.getNetworkTimeseriesNonUpdated();
        for(NetworkTimeseries net: networkTimeseries){
            EventBus.getDefault().post(new UploadEvent(net));
        }
        List<RunningApplicationTimeseries> runningApplicationTimeseries = oba.getRunningApplicationTimeseriesNonUpdated();
        for(RunningApplicationTimeseries rap: runningApplicationTimeseries){
            EventBus.getDefault().post(new UploadEvent(rap));
        }
        List<ScreenOnTimeseries> screenOnTimeseries = oba.getScreenOnTimeseriesNonUpdated();
        for(ScreenOnTimeseries scr: screenOnTimeseries){
            EventBus.getDefault().post(new UploadEvent(scr));
        }
        List<TrackTimeseries> trackTimeseries = oba.getTrackTimeseriesNonUpdated();
        for(TrackTimeseries track: trackTimeseries){
            EventBus.getDefault().post(new UploadEvent(track));
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String Session = prefs.getString("Session", null);
        if(ParseUser.getCurrentUser() == null){
            String userName = prefs.getString("Username", "");//"No name defined" is the default value.
            String password = prefs.getString("Password", ""); //0 is the default value.

            if (userName.equals("") == false && password.equals("") == false) {
                ParseUser.logInInBackground(userName, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.
//                            Intent i = new Intent(Application.getContext(), MapsActivity.class);
//                            startActivity(i);
//                            finish();
                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            Toast.makeText(Application.getContext(), "Wrong Email or Password", Toast.LENGTH_LONG);

                        }
                    }
                });
            } else {
//                Intent i = new Intent(Application.getContext(), LoginActivity.class);
//                startActivity(i);
//                finish();
            }
        }else{
            if(!ParseUser.getCurrentUser().isAuthenticated())
            {
//                Intent i = new Intent(Application.getContext(), LoginActivity.class);
//                startActivity(i);
//                finish();
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
