package de.dennis.mobilesensing.Uploader;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.UploadEvent;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition.GActivityTimeseries;
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
          "basetime": 2018....
          "name": "Energie",
          "icon": "Icons/smarthome/default_18.png",
          "valueTypes": [
            {
              "name": "Leistung",
              "type": "Number"
            }
            ],
          "user": "martin",
          "values": [
                {
                    "date" : 12345678
                    "value" : [ 12,234,2324,324]
                },
                 {
                    "date" : 12345678
                    "value" : [ 12,234,2324,324]
                }
          ]
            }
        */  try {
            ObjectBoxAdapter oba = new ObjectBoxAdapter();
            //GLocation
            if (st.getClass().getName().equals(GLocationTimeseries.class.getName())) {
                GLocationTimeseries glocTimeseries = (GLocationTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.location");
                po.put("basetime",glocTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "location");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "GeoJSON");
                jo.put("type","Geo");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(GLocationsObject gLocationsObject: glocTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [
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
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",gLocationsObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    JSONObject valueObject = new JSONObject();
                    valueObject.put("type","Feature");
                    JSONObject geo = new JSONObject();
                    geo.put("type","Point");
                    JSONArray coords = new JSONArray();
                    coords.put(gLocationsObject.lng);
                    coords.put(gLocationsObject.lat);
                    geo.put("coordinates",coords);
                    valueObject.put("geometry",geo);
                    JSONObject props = new JSONObject();
                    props.put("speed",gLocationsObject.speed);
                    valueObject.put("properties",props);
                    valueArray.put(valueObject);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                glocTimeseries.uploaded = true;
                oba.updateSensorTimeseries(glocTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(glocTimeseries));
            }
            //Network
            if (st.getClass().getName().equals(NetworkTimeseries.class.getName())) {
                NetworkTimeseries netTimeseries = (NetworkTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.network");
                po.put("basetime",netTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "network");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "NetworkType");
                jo.put("type","String");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(NetworkObject networkObject: netTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",networkObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(networkObject.networkType);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                netTimeseries.uploaded = true;
                oba.updateSensorTimeseries(netTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(netTimeseries));
            }
            //Activity
            if (st.getClass().getName().equals(ActivityTimeseries.class.getName())) {
                ActivityTimeseries actTimeseries = (ActivityTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.activity");
                po.put("basetime",actTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "activity");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "ActivityName");
                jo.put("type","String");
                ja.put(jo);
                jo.put("name", "ActivityProbability");
                jo.put("type","Number");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(ActivityObject actObject: actTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",actObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(actObject.activity);
                    valueArray.put(actObject.probability);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                actTimeseries.uploaded = true;
                oba.updateSensorTimeseries(actTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(actTimeseries));
            }
            //RunningApplication
            if (st.getClass().getName().equals(RunningApplicationTimeseries.class.getName())) {
                RunningApplicationTimeseries rapTimeseries = (RunningApplicationTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.app");
                po.put("basetime",rapTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "app");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "ApplicationName");
                jo.put("type","String");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(RunningApplicationObject rapObject: rapTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",rapObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(rapObject.applicationName);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                rapTimeseries.uploaded = true;
                oba.updateSensorTimeseries(rapTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(rapTimeseries));
            }
            //ScreenOn
            if (st.getClass().getName().equals(ScreenOnTimeseries.class.getName())) {
                ScreenOnTimeseries scrTimeseries = (ScreenOnTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.screen");
                po.put("basetime",scrTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "screen");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "ScreenOn");
                jo.put("type","Boolean");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(ScreenOnObject scrObject: scrTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",scrObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(scrObject.screenOn);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                scrTimeseries.uploaded = true;
                oba.updateSensorTimeseries(scrTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(scrTimeseries));
            }
            //Track
            if (st.getClass().getName().equals(TrackTimeseries.class.getName())) {
                TrackTimeseries trackTimeseries = (TrackTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.track");
                po.put("basetime",trackTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "track");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "StartTimestamp");
                jo.put("type","Number");
                ja.put(jo);
                jo.put("name", "EndTimestamp");
                jo.put("type","Number");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(TrackObject trackObject: trackTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",trackObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(trackObject.timestamp);
                    valueArray.put(trackObject.endTimestamp);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                trackTimeseries.uploaded = true;
                oba.updateSensorTimeseries(trackTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(trackTimeseries));
            }
            //GActivityTimeseries
            if (st.getClass().getName().equals(GActivityTimeseries.class.getName())) {
                GActivityTimeseries gActTimeseries = (GActivityTimeseries) st;
                ParseObject po = new ParseObject("SensingUpload");
                po.put("sensorid","mobilesensing.ganesha.gActivityTransition");
                po.put("basetime",gActTimeseries.timestamp_day);
                po.put("parent", new JSONArray());
                po.put("meta", new JSONObject());
                po.put("name", "gActivityTransition");
                po.put("icon", "Icons/smarthome/default_18.png");
                JSONArray ja = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("name", "StartTimestamp");
                jo.put("type","Number");
                ja.put(jo);
                jo.put("name", "EndTimestamp");
                jo.put("type","Number");
                ja.put(jo);
                jo.put("name", "ActivityName");
                jo.put("type","String");
                ja.put(jo);
                po.put("valueTypes",ja );
                po.put("user", ParseUser.getCurrentUser().getUsername());
                JSONArray values = new JSONArray();
                for(GActivityObject gActObject: gActTimeseries.values){
                    /*
                     {
                    "date" : 12345678
                    "value" : [WIFI
                    ]
                    }
                     */
                    JSONObject object = new JSONObject();
                    object.put("date",gActObject.timestamp);
                    JSONArray valueArray = new JSONArray();
                    valueArray.put(gActObject.timestamp);
                    valueArray.put(gActObject.endtime);
                    valueArray.put(gActObject.activity);
                    object.put("value",valueArray);
                    values.put(object);
                }
                po.put("values",values);
                gActTimeseries.uploaded = true;
                oba.updateSensorTimeseries(gActTimeseries);
                po.saveInBackground(updateSensorTimeseriesUpdated(gActTimeseries));
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
        st.uploaded = false;
        oba.updateSensorTimeseries(st);
    }
}
