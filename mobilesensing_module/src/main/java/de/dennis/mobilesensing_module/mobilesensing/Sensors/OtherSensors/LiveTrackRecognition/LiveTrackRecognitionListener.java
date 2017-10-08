package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.LiveTrackRecognition;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.TrackEndEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.PermissionActivity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocationListener.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocationListener.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.TrackListener.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;

/**
 * Created by Dennis on 28.08.2017.
 */

public class LiveTrackRecognitionListener {
    public LiveTrackRecognitionListener(){

    }

    public void start(){
        EventBus.getDefault().register(this);
    }

    // This method will be called when a new SensorDataEvent arrived
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(SensorDataEvent event) {
            if(event.data.getClass().getName().equals(GLocationsObject.class.getName())){
                //10 Locations in folge??
                ObjectBoxAdapter oba = new ObjectBoxAdapter();
                SharedPreferences prefs = Module.getContext().getSharedPreferences("TrackRecognition", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Date d = new Date();
                GregorianCalendar g = new GregorianCalendar();
                g.setTimeInMillis(d.getTime());
                //2016-05-23T16:00:00.000Z
                String timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
                List<GLocationTimeseries> gLocationTimeseriesList = oba.getGLocationTimeseries(timestamp_day,false);
                if(gLocationTimeseriesList.size()>0){
                    GLocationTimeseries gLocationTimeseries = gLocationTimeseriesList.get(0);
                    List<GLocationsObject> gLocationsObjects = gLocationTimeseries.getValues();
                    // Erst schauen ob aktueller status fahrt oder stop
                    // Dann gucken ob neue location abweicht bzw. 10 gleiche locations gesammelt wurden
                    if(gLocationsObjects.size() > 1){
                        GLocationsObject gLocationsObject1 = gLocationsObjects.get(gLocationsObjects.size()-2);
                        GLocationsObject gLocationsObject2 = gLocationsObjects.get(gLocationsObjects.size()-1);
                        float[] results = new float[1];
                        Location.distanceBetween(gLocationsObject1.getLat(),gLocationsObject1.getLng(),gLocationsObject2.getLat(),gLocationsObject2.getLng(),results);
                        if(results.length > 0){
                            if(Math.abs(results[0]) >= 50){
                                if(prefs.getLong("startTimestamp",0)>0){
                                    editor.putLong("endTimestamp",0);
                                    editor.putLong("stopCounter",0);
                                }else{
                                    editor.putLong("startTimestamp",gLocationsObject2.getTimestamp());
                                }
                            }else{
                                if(prefs.getLong("endTimestamp",0) >0){
                                    int counter = prefs.getInt("stopCounter",0)+1;
                                    if(counter >= 10){
                                        counter = 0;
                                        //new Timeseries *******************************************************************
                                        Long endTimestamp = prefs.getLong("endTimestamp",0);
                                        Long startTimestamp = prefs.getLong("startTimestamp",0);
                                        TrackObject trackObject = new TrackObject(startTimestamp,endTimestamp);
                                        //Send Event
                                        EventBus.getDefault().post(new TrackEndEvent(trackObject));
                                        EventBus.getDefault().post(new SensorDataEvent(trackObject));
                                        //**********************************************************************************
                                        editor.putLong("endTimestamp",0);
                                        editor.putLong("startTimestamp",0);
                                    }
                                    editor.putInt("stopCounter",counter);
                                }else{
                                    editor.putLong("endTimestamp",gLocationsObject2.getTimestamp());
                                    editor.putInt("stopCounter",1);
                                }
                            }
                            editor.apply();
                        }
                    }
                    // EventBus.getDefault().post(new SensorDataEvent(st));
                }
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TrackEndEvent event) {
        if(event.getClass().equals(TrackEndEvent.class)){
            GLocationsObject gLocationsObject = (GLocationsObject)event.data;
            GregorianCalendar g = new GregorianCalendar();
            g.setTimeInMillis(gLocationsObject.getTimestamp());
            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(Module.getContext())
                    // notification icon.setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Track Notification!") // title for notification
                    .setContentText("Hello word") // message for notification
                    .setAutoCancel(true); // clear notification after click
            Intent intent = new Intent(Module.getContext(), PermissionActivity.class);
            PendingIntent pi = PendingIntent.getActivity(Module.getContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) Module.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    public void stop() {
        EventBus.getDefault().unregister(this);
    }
}
