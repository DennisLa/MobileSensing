package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.Date;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Module;

/**
 * Created by Dennis on 11.03.2017.
 */
public class RunningApplicationListener extends BroadcastReceiver {
    private String top;
    private String TAG = RunningApplicationListener.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //For versions less than lollipop
            ActivityManager am = ((ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(5);
            if(taskInfo.size() == 0){
                Date d = new Date();
                SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                String runningApp = "";
                if(!prefs.getString("RunningApp","").equals(runningApp))
                {
                    //TODO StorageHelper.openDBConnection().save2RunningAppication(new RunningApplication(runningApp, d.getTime()));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("RunningApp",runningApp);
                    editor.apply();
                }
            }else{
                top = taskInfo.get(0).topActivity.getPackageName();
                Date x = new Date();
                SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                String runningApp = top;
                if(!prefs.getString("RunningApp","").equals(runningApp))
                {
                    //TODO StorageHelper.openDBConnection().save2RunningAppication(new RunningApplication(top,x.getTime()));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("RunningApp",runningApp);
                    editor.apply();
                }
                Log.d(TAG, "top app = " + top);
            }
        } else { //For versions Lollipop and above
            List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(context);
            Log.d(TAG,"Processes.size= "+processes.size());
            if(processes.size() == 0)
            {
                Date d = new Date();
                SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                String runningApp = "";
                if(!prefs.getString("RunningApp","").equals(runningApp))
                {
                    //TODO StorageHelper.openDBConnection().save2RunningAppication(new RunningApplication(runningApp, d.getTime()));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("RunningApp",runningApp);
                    editor.apply();
                }
            }
            for (int i = 0; i <= processes.size() - 1; i++) {
                Log.d(TAG,i+". "+ processes.get(i).getPackageName());
                Date x = new Date();
                if(!processes.get(i).getPackageName().equals("com.google.android.gms"))
                {
                    SharedPreferences prefs = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                    String runningApp = processes.get(i).getPackageName();
                    if(!prefs.getString("RunningApp","").equals(runningApp))
                    {
                        //TODO StorageHelper.openDBConnection().save2RunningAppication(new RunningApplication(processes.get(i).getPackageName(), x.getTime()));
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("RunningApp",runningApp);
                        editor.apply();
                    }
                }
            }
        }
    }
}
