package de.dennis.mobilesensing.Uploader;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network.NetworkObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network.NetworkTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication.RunningApplicationObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication.RunningApplicationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOn.ScreenOnObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOn.ScreenOnTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import io.objectbox.Box;

/**
 * Created by Dennis on 17.09.2017.
 */

public class ParseUploader {

    public ParseUploader()
    {
        EventBus.getDefault().register(this);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(UploadEvent event) {
        uploadToParse(event.data);
    }

    public void uploadToParse(SensorTimeseries st)
        {
        /*
            {
          "id": "owcore.test.energy",
          "meta": {},
          "name": "Energie",
          "icon": "Icons/smarthome/default_18.png",
          "valueTypes": [
            {
              "name": "Leistung",
              "type": "Number"
            }
            ],
          "user": "martin",
          "values": []
            }
        */  try {
            ObjectBoxAdapter oba = new ObjectBoxAdapter();
            //GLocation
            //TODO Classname
            //TODO Add UID
            if (st.getClass().getName().equals(GLocationTimeseries.class.getName())) {
                GLocationTimeseries glocTimeseries = (GLocationTimeseries) st;
                ParseObject po = new ParseObject("Location");
                po.put("basetime",glocTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "Location");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "GeoJSON");
                jo.put("type","Geo");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","Timestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(GLocationsObject gLocationsObject: glocTimeseries.getValues()){
                    /*
                    {
                      "type": "Feature",
                      "geometry": {
                        "type": "Point",
                        "coordinates": [125.6, 10.1]
                      },
                      "properties": {
                        "name": "Dinagat Islands"
                     }
}
                    */
                    JSONObject value = new JSONObject();
                    value.put("type","Feature");
                    JSONObject geo = new JSONObject();
                    geo.put("type","Point");
                    JSONArray coords = new JSONArray();
                    coords.put(gLocationsObject.getLat());
                    coords.put(gLocationsObject.getLng());
                    geo.put("coordinates",coords);
                    value.put("geometry",geo);
                    JSONObject props = new JSONObject();
                    props.put("timestamp",gLocationsObject.getTimestamp());
                    value.put("properties",props);
                    values.put(value);
                }
                po.put("values",values);
                glocTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(glocTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(glocTimeseries));
            }
            //Network
            if (st.getClass().getName().equals(NetworkTimeseries.class.getName())) {
                NetworkTimeseries netTimeseries = (NetworkTimeseries) st;
                ParseObject po = new ParseObject("Network");
                po.put("basetime",netTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "Network");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "NetworkType");
                jo.put("type","String");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","Timestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(NetworkObject netObject: netTimeseries.getValues()){
                    JSONObject value = new JSONObject();
                    value.put("NetworkType",netObject.getNetworkType());
                    value.put("Timestamp",netObject.getTimestamp());
                    values.put(value);
                }
                po.put("values",values);
                netTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(netTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(netTimeseries));
            }
            //Activity
            if (st.getClass().getName().equals(ActivityTimeseries.class.getName())) {
                ActivityTimeseries actTimeseries = (ActivityTimeseries) st;
                ParseObject po = new ParseObject("Activity");
                po.put("basetime",actTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "Activity");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name","ActivityName");
                jo.put("type","String");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","Timestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(ActivityObject actObject: actTimeseries.getValues()){
                    JSONObject value = new JSONObject();
                    value.put("ActivityName",actObject.getActivity());
                    value.put("Timestamp",actObject.getTimestamp());
                    values.put(value);
                }
                po.put("values",values);
                actTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(actTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(actTimeseries));
            }
            //RunningApplication
            if (st.getClass().getName().equals(RunningApplicationTimeseries.class.getName())) {
                RunningApplicationTimeseries rapTimeseries = (RunningApplicationTimeseries) st;
                ParseObject po = new ParseObject("RunningApplication");
                po.put("basetime",rapTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "RunningApplication");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name","ApplicationName");
                jo.put("type","String");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","Timestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(RunningApplicationObject rapObject: rapTimeseries.getValues()){
                    JSONObject value = new JSONObject();
                    value.put("ActivityName",rapObject.getApplicationName());
                    value.put("Timestamp",rapObject.getTimestamp());
                    values.put(value);
                }
                po.put("values",values);
                rapTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(rapTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(rapTimeseries));
            }
            //ScreenOn
            if (st.getClass().getName().equals(ScreenOnTimeseries.class.getName())) {
                ScreenOnTimeseries scrTimeseries = (ScreenOnTimeseries) st;
                ParseObject po = new ParseObject("ScreenOn");
                po.put("basetime",scrTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "ScreenOn");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name","ScreenOn");
                jo.put("type","Boolean");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","Timestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(ScreenOnObject scrObject: scrTimeseries.getValues()){
                    JSONObject value = new JSONObject();
                    value.put("ScreenOn",scrObject.getScreenOn());
                    value.put("Timestamp",scrObject.getTimestamp());
                    values.put(value);
                }
                po.put("values",values);
                scrTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(scrTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(scrTimeseries));
            }
            //Track
            if (st.getClass().getName().equals(TrackTimeseries.class.getName())) {
                TrackTimeseries trackTimeseries = (TrackTimeseries) st;
                ParseObject po = new ParseObject("Track");
                po.put("basetime",trackTimeseries.getTimestamp_day());
                po.put("meta", new JSONObject());
                po.put("name", "Track");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name","StartTimestamp");
                jo.put("type","Number");
                ja.put(jo);
                JSONObject jo2 = new JSONObject();
                jo2.put("name","EndTimestamp");
                jo2.put("type","Number");
                ja.put(jo2);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(TrackObject trackObject: trackTimeseries.getValues()){
                    JSONObject value = new JSONObject();
                    value.put("StartTimestamp",trackObject.getStartTimestamp());
                    value.put("EndTimestamp",trackObject.getEndTimestamp());
                    values.put(value);
                }
                po.put("values",values);
                trackTimeseries.setUploaded(true);
                oba.updateSensorTimeseries(trackTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(trackTimeseries));
            }
        }catch(Exception e){
            undoUpdated(st);
        }
    }

    public SaveCallback updateSensorTimeseriesUpdated(final SensorTimeseries st){
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                   // da.deleteTimeseries(st.getTimestamp_day(),st.getSensor_info().getSensor_name());
                }else{
                    //TODO
                    undoUpdated(st);
                }
            }
        };
    }
    public void undoUpdated(SensorTimeseries st){
        ObjectBoxAdapter oba = new ObjectBoxAdapter();

        //GLocation
        if (st.getClass().getName().equals(GLocationTimeseries.class.getName())) {
            GLocationTimeseries glt = (GLocationTimeseries) st;
            glt.setUploaded(false);
            oba.updateSensorTimeseries(glt);
        }
        //Network
        if (st.getClass().getName().equals(NetworkTimeseries.class.getName())) {
            NetworkTimeseries nts = (NetworkTimeseries) st;
            nts.setUploaded(false);
            oba.updateSensorTimeseries(nts);
        }
        //Activity
        if (st.getClass().getName().equals(ActivityTimeseries.class.getName())) {
            ActivityTimeseries act = (ActivityTimeseries) st;
            act.setUploaded(false);
            oba.updateSensorTimeseries(act);
        }
        //RunningApplication
        if (st.getClass().getName().equals(RunningApplicationTimeseries.class.getName())) {
            RunningApplicationTimeseries rap = (RunningApplicationTimeseries) st;
            rap.setUploaded(false);
            oba.updateSensorTimeseries(rap);
        }
        //ScreenOn
        if (st.getClass().getName().equals(ScreenOnTimeseries.class.getName())) {
            ScreenOnTimeseries scr = (ScreenOnTimeseries) st;
            scr.setUploaded(false);
            oba.updateSensorTimeseries(scr);
        }
        //Track
        if (st.getClass().getName().equals(TrackTimeseries.class.getName())) {
            TrackTimeseries track = (TrackTimeseries) st;
            track.setUploaded(false);
            oba.updateSensorTimeseries(track);
        }
    }
}
