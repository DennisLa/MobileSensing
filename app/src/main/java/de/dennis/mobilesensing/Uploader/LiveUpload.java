package de.dennis.mobilesensing.Uploader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing.UI.ChatbotActivity;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network.NetworkObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication.RunningApplicationObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication.RunningApplicationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOn.ScreenOnObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Dennis on 13.05.2018.
 */

public class LiveUpload {

    public LiveUpload() {
        EventBus.getDefault().register(this);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(SensorDataEvent event) {
        uploadSensorData(event.data);
        makeNotification(event.data);
    }

    public void makeNotification(SensorObject so) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Application.getContext(), "notify_001");
        Intent ii = new Intent(Application.getContext().getApplicationContext(), ChatbotActivity.class);
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

    public void uploadSensorData(final SensorObject data){
        try {
            URL url = new URL("http://85.214.113.77:8444");
            JSONObject jsonParam = new JSONObject();
            SharedPreferences prefs = Application.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            jsonParam.put("chatId",prefs.getString("chatID",""));
            long lastUpload = prefs.getLong("lastLiveUpload",0);
            long currUpload = 0;
            if (data.getClass().getName().equals(GActivityObject.class.getName())) {
                GActivityObject obj = (GActivityObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type",GActivityObject.class.getSimpleName());
                jsonParam.put("endTimestamp",obj.getEndtime());
                jsonParam.put("activity",obj.getActivity());
                currUpload = obj.getTimestamp();
            }
            if(data.getClass().getName().equals(ActivityObject.class.getName())){
                ActivityObject obj = (ActivityObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type",ActivityObject.class.getSimpleName());
                jsonParam.put("activity",obj.getActivity());
                currUpload = obj.getTimestamp();
            }
            if(data.getClass().getName().equals(GLocationsObject.class.getName())){
                GLocationsObject obj = (GLocationsObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type",GLocationsObject.class.getSimpleName());
                jsonParam.put("lat",obj.getLat());
                jsonParam.put("lng",obj.getLng());
                jsonParam.put("speed",obj.speed);
                currUpload = obj.getTimestamp();
            }
            if(data.getClass().getName().equals(NetworkObject.class.getName())){
                NetworkObject obj = (NetworkObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type",NetworkObject.class.getSimpleName());
                jsonParam.put("networkType",obj.getNetworkType());
                currUpload = obj.getTimestamp();
            }
            if(data.getClass().getName().equals(RunningApplicationObject.class.getName())){
                RunningApplicationObject obj = (RunningApplicationObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type", RunningApplicationObject.class.getSimpleName());
                jsonParam.put("appName",obj.getApplicationName());
                currUpload = obj.getTimestamp();
            }
            if(data.getClass().getName().equals(ScreenOnObject.class.getName())){
                ScreenOnObject obj = (ScreenOnObject) data;
                jsonParam.put("timestamp",obj.getTimestamp());
                jsonParam.put("type",ScreenOnObject.class.getSimpleName());
                jsonParam.put("screenOn",obj.getScreenOn());
                currUpload = obj.getTimestamp();
            }
            // Log.i("JSON", jsonParam.toString());

            if(lastUpload <= currUpload-1000){
                Log.d("UPLOAD","Live Upload"+data.getClass().getSimpleName());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("lastLiveUpload",currUpload);
                editor.apply();
                // HttpClient
                HttpClient httpClient = new DefaultHttpClient();

                // post header
                HttpPost httpPost = new HttpPost("http://85.214.113.77:8444");

                StringEntity se = new StringEntity(jsonParam.toString());

                //sets the post request as the resulting string
                httpPost.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // execute HTTP post request
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {

                    String responseStr = EntityUtils.toString(resEntity).trim();
                    Log.v("UPLOADER", "Response: " +  responseStr);

                    // you can add an if statement here and do other actions based on the response
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
