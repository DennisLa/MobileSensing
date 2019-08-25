package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ClusterService;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.ParseObject;

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
//        if((new Date().getTime() - 3600000*24)>= lastTime){
        if((new Date().getTime() - 1000*60)>= lastTime){ // each 1 hour
            ParseObject gameScore = new ParseObject("GameScore");
            gameScore.put("score", 1);
            gameScore.put("playerName", "From ClusterJobService each 1h!");
            gameScore.put("cheatMode", false);
            gameScore.saveInBackground();
            Intent service = new Intent(getApplicationContext(), ClusterService.class);
            getApplicationContext().startService(service);
            //SharedPreferences.Editor editor = prefs.edit();
            //editor.putLong("LastTime",new Date().getTime());
            //editor.apply();
            Module.saveltc(new Date().getTime());
//            jobFinished(jobParameters, true);
        }
        return true;
    }

    private void registerRefreshJob(long l) {
        ComponentName serviceComponent = new ComponentName(Module.getContext(), ClusterJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
//        builder.setMinimumLatency(1000*10); // wait at least 3600000
        builder.setOverrideDeadline(l); // maximum delay 600000
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        JobScheduler jobScheduler = (JobScheduler) Module.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1);
        gameScore.put("playerName", "Stopped!");
        gameScore.put("cheatMode", false);
        gameScore.saveInBackground();
        return true; // return true to reschedule the job
    } // 21.08.2019, i changed it to true, it was false
}
