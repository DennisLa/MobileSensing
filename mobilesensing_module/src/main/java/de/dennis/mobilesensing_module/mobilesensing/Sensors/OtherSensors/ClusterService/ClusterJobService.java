package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ClusterService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Date;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.ms.ptenabler.locationtools.ClusterService;

/**
 * Created by Dennis on 27.11.2017.
 */

public class ClusterJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //SharedPreferences prefs = Module.getContext().getSharedPreferences("Cluster",MODE_PRIVATE);
        //long lastTime = prefs.getLong("LastTime",0L);
        long lastTime = Module.getLtc();
        if((new Date().getTime() - 3600000*24)>= lastTime){
            Intent service = new Intent(getApplicationContext(), ClusterService.class);
            getApplicationContext().startService(service);
            //SharedPreferences.Editor editor = prefs.edit();
            //editor.putLong("LastTime",new Date().getTime());
            //editor.apply();
            Module.saveltc(new Date().getTime());
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
