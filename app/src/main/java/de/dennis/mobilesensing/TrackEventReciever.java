package de.dennis.mobilesensing;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.GregorianCalendar;

import de.dennis.mobilesensing.UI.TrackRatingActivity;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.TrackEndEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.PermissionActivity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackObject;


/**
 * Created by Dennis on 21.10.2017.
 */

public class TrackEventReciever {

    public TrackEventReciever(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(TrackEndEvent event) {
        TrackObject trackObject = (TrackObject) event.data;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(trackObject.timestamp);
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(Module.getContext())
                // notification icon.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Neue Fahrten!") // title for notification
                .setContentText("Es können neue Fahrten bewertet werden.") // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(Module.getContext(), TrackRatingActivity.class);
        PendingIntent pi = PendingIntent.getActivity(Module.getContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) Module.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
