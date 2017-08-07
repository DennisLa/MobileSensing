package de.dennis.mobilesensing.UploadService;

import android.os.AsyncTask;
import android.util.Log;

import com.baasbox.android.BaasUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplication;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService.ScreenOn;


/**
 * Created by Dennis on 01.06.2017.
 */
public class MongoNodeUploader {
    private static boolean isUploaded;
    /*public long startUpload(final long from, long to)
    {
        isUploaded = true;
        //Upload Activities
        List<wActivity> activities = StorageHelper.openDBConnection().getAllActivities(from, to, false);
        wActivity[] arr = new wActivity[activities.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i]= activities.get(i);
        }
        try {
            JSONArray json = new JSONArray(arr);
            Log.d("UPLOADER_JSON", json.toString());
            new AsyncUpload().execute(json.toString(),"ACTIVITY", BaasUser.current().getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Upload Call
        List<wCall> calls = StorageHelper.openDBConnection().getAllCalls(from, to, false);

        //Upload ScreenOn
        List<ScreenOn> scrOns = StorageHelper.openDBConnection().getAllScreenOn(from, to, false);

        //Upload Location
        List<wLocation> locations = StorageHelper.openDBConnection().getAllHistoryLocs(from, to, false);

        //Upload Network
        List<wNetwork> networks = StorageHelper.openDBConnection().getAllNetworks(from, to, false);

        //Upload RunningApplication
        List<RunningApplication> apps = StorageHelper.openDBConnection().getAllRunningApplications(from, to, false);

        //Upload Track
        List<wTrack> tracks = StorageHelper.openDBConnection().getAllTracks(from, to, false);

        //Check
        if(isUploaded == true)
        {
            return to;
        }else{
            return from;
        }

    }
    */

    /*
    public HttpResponse postData(String values, String sensor, String session) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://141.99.12.45:3000/sendData");
        httppost.addHeader("sesssion",session);
        try {
            List body = new ArrayList();
            body.add(new BasicNameValuePair("values", values));
            body.add(new BasicNameValuePair("sensor", sensor));
            httppost.setEntity(new UrlEncodedFormEntity(body));
            HttpResponse response = httpclient.execute(httppost);
            return response;
        } catch (ClientProtocolException e) {
            // process execption
        } catch (IOException e) {
            // process execption
        }
    }
    */

    /*
    private class AsyncUpload extends AsyncTask<String,Void,HttpResponse> {
        long startTimestamp = 0;
        long endTimestamp = 0;
        String sensor = "";
        @Override
        protected HttpResponse doInBackground(String... params) {
            startTimestamp = Long.parseLong(params[3]);
            endTimestamp  = Long.parseLong(params[4]);
            sensor = params[1];
            return postData(params[0], params[1], params[2]);
        }
        @Override
        protected void onPostExecute(HttpResponse result){
            if(result.getStatusLine().getStatusCode() == 200){

                if(sensor.equals("ACTIVITY"){
                    StorageHelper.openDBConnection().deleteActivities(startTimestamp,endTimestamp);
                }

            }
        }
    }
    */
}
