package de.dennis.mobilesensing.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing.UI.MainActivity;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Dennis on 13.05.2018.
 */

public class NotificationListener {

    public NotificationListener() {
        EventBus.getDefault().register(this);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(SensorDataEvent event) {
        makeNotification(event.data);
    }

    public void makeNotification(SensorObject so) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Application.getContext(), "notify_001");
        Intent ii = new Intent(Application.getContext().getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Application.getContext(), 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("New Sensor Data");
        mBuilder.setContentText(so.getClass().getSimpleName());
        mBuilder.setWhen((new Date().getTime()));
        mBuilder.setPriority(Notification.PRIORITY_MAX);


        NotificationManager mNotificationManager =
                (NotificationManager) Application.getContext().getSystemService(NOTIFICATION_SERVICE);


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel  channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }*/

        mNotificationManager.notify(0, mBuilder.build());
    }
}
