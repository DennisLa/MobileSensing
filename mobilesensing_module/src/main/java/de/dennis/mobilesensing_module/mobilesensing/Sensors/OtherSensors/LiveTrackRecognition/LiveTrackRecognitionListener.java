package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.LiveTrackRecognition;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.TrackEndEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.PermissionActivity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.DataAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GeoPointEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.LineStringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;

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
        if(event.getClass().equals(SensorDataEvent.class)){
            if(event.data.getSensor_info().getSensor_name().equals("Location")){
                //10 Locations in folge??
                DataAdapter da = new DataAdapter();
                SharedPreferences prefs = Module.getContext().getSharedPreferences("TrackRecognition", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                //TODO insert today
                Date d = new Date();
                GregorianCalendar g = new GregorianCalendar();
                g.setTimeInMillis(d.getTime());
                //2016-05-23T16:00:00.000Z
                String timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
                SensorTimeseries st = da.getSensorTimeseries(timestamp_day,"Location");
                if(st!= null){
                    List<SensorValue> lsv = st.getValues();
                    // Erst schauen ob aktueller status fahrt oder stop
                    // Dann gucken ob neue location abweicht bzw. 10 gleiche locations gesammelt wurden
                    if(lsv.size() > 1){
                        SensorValue sv1 = lsv.get(lsv.size()-2);
                        GeoPointEntity gp1 = sv1.getGeoPointEntities().get(0);
                        SensorValue sv2 = lsv.get(lsv.size()-1);
                        GeoPointEntity gp2 = sv2.getGeoPointEntities().get(0);
                        float[] results = new float[1];
                        Location.distanceBetween(gp1.getLat(),gp1.getLng(),gp2.getLat(),gp2.getLng(),results);
                        if(results.length > 0){
                            if(Math.abs(results[0]) >= 50){
                                if(prefs.getLong("startTimestamp",0)>0){
                                    editor.putLong("endTimestamp",0);
                                    editor.putLong("stopCounter",0);
                                }else{
                                    editor.putLong("startTimestamp",sv2.getTimestamp());
                                }
                            }else{
                                if(prefs.getLong("endTimestamp",0) >0){
                                    int counter = prefs.getInt("stopCounter",0)+1;
                                    if(counter >= 10){
                                        counter = 0;
                                        //new Timeseries *******************************************************************
                                        //Init SensorInfo
                                        SensorInfo si = new SensorInfo("Track","Live Tracking Track");
                                        //Add  one ValueInfo for each measure
                                        si.addValueInfo(new ValueInfo("GeoJSON","GeoJSON describing a Line String","JSON"));
                                        //Init SensorValue
                                        Long tsLong = prefs.getLong("endTimestamp",0);
                                        SensorValue sv = new SensorValue(tsLong);
                                        //Add one StringEntitiy for each measure (same order)
                                        LineStringEntity lse = new LineStringEntity();
                                        for(SensorValue sensorvalue: lsv)
                                        {
                                            if(sensorvalue.getTimestamp() <= tsLong && sensorvalue.getTimestamp() >= prefs.getLong("startTimestamp",0)){
                                                GeoPointEntity gpe = sensorvalue.getGeoPointEntities().get(0);
                                                lse.addGeoPoint(new GeoPointEntity(gpe.getLat(),gpe.getLng()));
                                            }
                                        }
                                        sv.addLineStringEntity(lse);
                                        //Init Time Series
                                        SensorTimeseries set = new SensorTimeseries(tsLong,"TrackRecognition","TrackRecognition"+Module.getUser(),Module.getUser(),si,sv);
                                        //Send Event
                                        EventBus.getDefault().post(new TrackEndEvent(set));
                                        //**********************************************************************************
                                        editor.putLong("endTimestamp",0);
                                        editor.putLong("startTimestamp",0);
                                    }
                                    editor.putInt("stopCounter",counter);
                                }else{
                                    editor.putLong("endTimestamp",sv2.getTimestamp());
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TrackEndEvent event) {
        if(event.getClass().equals(TrackEndEvent.class)){
            GregorianCalendar g = new GregorianCalendar();
            g.setTimeInMillis(event.data.getTimestamp());
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
