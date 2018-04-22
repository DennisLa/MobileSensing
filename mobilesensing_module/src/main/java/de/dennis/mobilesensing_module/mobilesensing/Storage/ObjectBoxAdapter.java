package de.dennis.mobilesensing_module.mobilesensing.Storage;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.dennis.mobilesensing_module.R;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
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
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackTimeseries;
import io.objectbox.Box;

/**
 * Created by Dennis on 01.10.2017.
 */

public class ObjectBoxAdapter {
    final String TAG = "ObjectBoxAdapter";

    public void saveSensorObject(SensorObject so){
        Log.d("SAVESENSOROBJECT","New Object: "+so.getClass().getSimpleName()+ " Time: "+ new Date().toGMTString());
        //General Object
        try {
            Class<?> timeseriesClass = Class.forName(so.timeseriesClassName);
            Box timeseriesBox = Module.getBoxStore().boxFor(timeseriesClass);
            SensorTimeseries to = null;
            List<GActivityTimeseries>lst = timeseriesBox.getAll();
            for(SensorTimeseries ts: lst){
                if(ts.timestamp == getTimestampDay(so.timestamp)){
                    to = ts;
                }
            }
            if(to == null){
                long timestampDay = getTimestampDay(so.timestamp);
                Constructor con = timeseriesClass.getConstructor(long.class);
                Object o = con.newInstance(timestampDay);
                to = (SensorTimeseries) o;
                to.current = true;
                for(SensorTimeseries ts: lst){
                    ts.current = false;
                    timeseriesBox.put(ts);
                }
            }
            to.values.add(so);
            timeseriesBox.put(to);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void deleteSensorTimeseries(SensorTimeseries st){
        //General Object
        Box timeseriesBox = Module.getBoxStore().boxFor(st.getClass());
        timeseriesBox.remove(st);
    }

    public long getTimestampDay(long timestamp){
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
        g.set(GregorianCalendar.HOUR_OF_DAY,0);
        g.set(GregorianCalendar.MINUTE,0);
        g.set(GregorianCalendar.SECOND,0);
        g.set(GregorianCalendar.MILLISECOND,0);
        return g.getTimeInMillis();
    }

    public void updateSensorTimeseries(SensorTimeseries st) {
            Box timeseriesBox = Module.getBoxStore().boxFor(st.getClass());
            timeseriesBox.put(st);
    }

    public long updateSensorObject(SensorObject so){
        Box objectBox = Module.getBoxStore().boxFor(so.getClass());
        return objectBox.put(so);
    }

    public List<SensorTimeseries> getSensorTimeseries(String timestampDay, boolean onlyNonUpdated,String timeseriesClassName){
        try {
            Class<?> timeseriesClass = Class.forName(timeseriesClassName);
            Box timeseriesBox = Module.getBoxStore().boxFor(timeseriesClass);
            List<SensorTimeseries> sensorTimeseriesList = timeseriesBox.getAll();
            List<SensorTimeseries> sensorTimeseriesListResult = new ArrayList<>();
            for(SensorTimeseries sensorTimeseries: sensorTimeseriesList){
                if(onlyNonUpdated && !sensorTimeseries.uploaded){
                    if(sensorTimeseries.timestamp_day.equals(timestampDay)){
                        sensorTimeseriesListResult.add(sensorTimeseries);
                    }
                }else if(!onlyNonUpdated){
                    if(sensorTimeseries.timestamp_day.equals(timestampDay)){
                        sensorTimeseriesListResult.add(sensorTimeseries);
                    }
                }
            }
            return sensorTimeseriesListResult;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<SensorTimeseries>();
        }
    }

    public List<SensorTimeseries> getSensorTimeseriesNonUpdated(String timeseriesClassName){
        try {
            Class<?> timeseriesClass = Class.forName(timeseriesClassName);
            Box timeseriesBox = Module.getBoxStore().boxFor(timeseriesClass);
            List<SensorTimeseries> sensorTimeseriesList = timeseriesBox.getAll();
            List<SensorTimeseries> sensorTimeseriesListResult = new ArrayList<>();
            for(SensorTimeseries sensorTimeseries: sensorTimeseriesList){
                if(!sensorTimeseries.uploaded&& !sensorTimeseries.current){
                    sensorTimeseriesListResult.add(sensorTimeseries);
                }
            }
            return sensorTimeseriesListResult;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<SensorTimeseries>();
        }
    }

    public void deleteAllSensorTimeseriesOlder(long time) {
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        Resources res = Module.getContext().getResources();
        String[] arr = res.getStringArray(R.array.sensor_names);
        for(int i = 0; i < arr.length; i++){
            Class<?> timeseriesClass = null;
            try {
                timeseriesClass = Class.forName(arr[i]);
                Box timeseriesBox = Module.getBoxStore().boxFor(timeseriesClass);
                List<SensorTimeseries> sensorTimeseriesList = timeseriesBox.getAll();
                for(SensorTimeseries st: sensorTimeseriesList){
                    if(st.timestamp < time){
                        deleteSensorTimeseries(st);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        /*
        //GLocation
        Box gLocBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
        List<GLocationTimeseries> gLocList = gLocBox.getAll();
        for(GLocationTimeseries gLoc: gLocList){
            if(gLoc.getTimestamp() < time){
                deleteSensorTimeseries(gLoc);
            }
        }
        //GActivity
        Box gActivityBox = Module.getBoxStore().boxFor(GActivityTimeseries.class);
        List<GActivityTimeseries> gActivityList = gLocBox.getAll();
        for(GActivityTimeseries gActivity: gActivityList){
            if(gActivity.getTimestamp() < time){
                deleteSensorTimeseries(gActivity);
            }
        }
        //Activity
        Box actBox = Module.getBoxStore().boxFor(ActivityTimeseries.class);
        List<ActivityTimeseries> actList = actBox.getAll();
        for(ActivityTimeseries act: actList){
            if(act.getTimestamp() < time){
                deleteSensorTimeseries(act);
            }
        }
        //Network
        Box netBox = Module.getBoxStore().boxFor(NetworkTimeseries.class);
        List<NetworkTimeseries> netList = netBox.getAll();
        for(NetworkTimeseries net: netList){
            if(net.getTimestamp() < time){
                deleteSensorTimeseries(net);
            }
        }
        //RunningApplication
        Box runappBox = Module.getBoxStore().boxFor(RunningApplicationTimeseries.class);
        List<RunningApplicationTimeseries> runappList = runappBox.getAll();
        for(RunningApplicationTimeseries runapp: runappList){
            if(runapp.getTimestamp() < time){
                deleteSensorTimeseries(runapp);
            }
        }
        //ScreenOn
        Box screenBox = Module.getBoxStore().boxFor(ScreenOnTimeseries.class);
        List<ScreenOnTimeseries> screenList = screenBox.getAll();
        for(ScreenOnTimeseries screen: screenList){
            if(screen.getTimestamp() < time){
                deleteSensorTimeseries(screen);
            }
        }
        //Track
        Box trackBox = Module.getBoxStore().boxFor(TrackTimeseries.class);
        List<TrackTimeseries> trackList = trackBox.getAll();
        for(TrackTimeseries track: trackList){
            if(track.getTimestamp() < time){
                deleteSensorTimeseries(track);
            }
        }*/
    }
    //Methods for Clustering
    public List<GLocationsObject> getUnclusteredGLocations(long since, long until) {
        Box locationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<GLocationsObject> locationList = locationBox.getAll();
        List<GLocationsObject> unclusteredList = new ArrayList<>();
        for(GLocationsObject gloc: locationList){
            if(gloc.isClustered == false && gloc.timestamp > since && gloc.timestamp < until){
                unclusteredList.add(gloc);
            }
        }
        return unclusteredList;
    }

    public List<GLocationsObject> getGLocationTimeseries(long since, long until) {
        Box locationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<GLocationsObject> locationList = locationBox.getAll();
        List<GLocationsObject> retList = new ArrayList<>();
        for(GLocationsObject gloc: locationList){
            if(gloc.timestamp > since && gloc.timestamp < until){
                retList.add(gloc);
            }
        }
        return retList;
    }

    public List<ClusterObject> getAllClusterObjects() {
        Box clusterBox = Module.getBoxStore().boxFor(ClusterObject.class);
        List<ClusterObject> clusterList = clusterBox.getAll();
        return clusterList;
    }

    public GLocationsObject getGLocationObject(int id) {
        Box glocationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        if(id != 0){
            return (GLocationsObject) glocationBox.get(id);
        }
       return null;
    }

    public void deleteClusterObject(long id) {
        Box clusterBox = Module.getBoxStore().boxFor(ClusterObject.class);
        clusterBox.remove(id);
    }
}
