package de.dennis.mobilesensing_module.mobilesensing.Upload.DailyUpload;

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.SensingManager.SensorNames;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService.RunningApplication;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService.ScreenOn;
import de.dennis.mobilesensing_module.mobilesensing.Storage.DataAdapter;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;


/**
 * Created by Dennis on 01.06.2017.
 */
public class DailyUploader {
    public void startUpload(String username, String session, String url)
    {
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis((new Date()).getTime());
        String timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        DataAdapter da = new DataAdapter();
        //Upload Activities
        for(SensorNames sn: SensorNames.values()){
            List<SensorTimeseries> lst = da.getSensorTimeseriesOlder(timestamp_day, sn.name());
            for(SensorTimeseries st:lst){
                try {
                    st.setUser(username);
                    new AsyncUpload().execute(st.toJSON().toString(),session,st.getTimestamp_day(),url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //----------
    }
    public HttpResponse postData(String values, String session, String URL) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        httppost.addHeader("session",session);
        try {
            List body = new ArrayList();
            body.add(new BasicNameValuePair("values", values));
            httppost.setEntity(new UrlEncodedFormEntity(body));
            HttpResponse response = httpclient.execute(httppost);
            return response;
        } catch (ClientProtocolException e) {
            // process execption
        } catch (IOException e) {
            // process execption
        }
        return null;
    }

    private class AsyncUpload extends AsyncTask<String,Void,HttpResponse> {
        String timestamp_day = "";
        String sensorName = "";
        @Override
        protected HttpResponse doInBackground(String... params) {
            //st.toJSON().toString(),session,st.getTimestamp_day(),url
            timestamp_day = params[3];
            sensorName = params[1];
            return postData(params[0], params[1], params[3]);
        }
        @Override
        protected void onPostExecute(HttpResponse result){
            if(result.getStatusLine().getStatusCode() == 200){
                DataAdapter da = new DataAdapter();
                da.deleteTimeseries(timestamp_day,sensorName);
            }
        }
    }

}
