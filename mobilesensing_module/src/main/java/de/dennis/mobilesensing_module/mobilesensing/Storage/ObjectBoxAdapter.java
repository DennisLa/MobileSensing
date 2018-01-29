package de.dennis.mobilesensing_module.mobilesensing.Storage;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
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
        //GLocation
        if(so.getClass().getName().equals(GLocationsObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(GLocationsObject.class);
            GLocationsObject so_casted = (GLocationsObject) so;
            GLocationTimeseries to = null;//= (GLocationTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<GLocationTimeseries>lst = timeseriesBox.getAll();
            for(GLocationTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new GLocationTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(GLocationTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            /*objectBox.put(so_casted);
            ToMany<GLocationsObject> tmo = to.getValues();
            tmo.add(so_casted);*/
            to.values.add(so_casted);
            timeseriesBox.put(to);
            Log.d(TAG, "GLocation");
        }
        //Network
        if(so.getClass().getName().equals(NetworkObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(NetworkTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(NetworkObject.class);
            NetworkObject so_casted = (NetworkObject) so;
            NetworkTimeseries to = null;// (NetworkTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<NetworkTimeseries>lst = timeseriesBox.getAll();
            for(NetworkTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new NetworkTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(NetworkTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            /*objectBox.put(so_casted);
            ToMany<NetworkObject> tmo = to.getValues();
            tmo.add(so_casted);*/
            to.values.add(so_casted);
            timeseriesBox.put(to);
            Log.d(TAG, "Network");
        }
        //Activity
        if(so.getClass().getName().equals(ActivityObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(ActivityTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(ActivityObject.class);
            ActivityObject so_casted = (ActivityObject) so;
            ActivityTimeseries to = null; //=(ActivityTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<ActivityTimeseries>lst = timeseriesBox.getAll();
            for(ActivityTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new ActivityTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(ActivityTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            to.values.add(so_casted);
            //to.values.add(new ActivityObject(so_casted.getTimestamp(),so_casted.getActivity()));
            timeseriesBox.put(to);
            Log.d(TAG, "Activity");
        }
        //RunningApplication
        if(so.getClass().getName().equals(RunningApplicationObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(RunningApplicationTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(RunningApplicationObject.class);
            RunningApplicationObject so_casted = (RunningApplicationObject) so;
            RunningApplicationTimeseries to = null;// (RunningApplicationTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<RunningApplicationTimeseries>lst = timeseriesBox.getAll();
            for(RunningApplicationTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new RunningApplicationTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(RunningApplicationTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            /*objectBox.put(so_casted);
            ToMany<RunningApplicationObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);*/
            to.values.add(so_casted);
            timeseriesBox.put(to);
            Log.d(TAG, "RunningApplication");
        }
        //ScreenOn
        if(so.getClass().getName().equals(ScreenOnObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(ScreenOnTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(ScreenOnObject.class);
            ScreenOnObject so_casted = (ScreenOnObject) so;
            ScreenOnTimeseries to = null;//(ScreenOnTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<ScreenOnTimeseries>lst = timeseriesBox.getAll();
            for(ScreenOnTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new ScreenOnTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(ScreenOnTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            to.values.add(so_casted);
            timeseriesBox.put(to);
            Log.d(TAG, "ScreenOn");
        }
        //Track
        if(so.getClass().getName().equals(TrackObject.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(TrackTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(TrackObject.class);
            TrackObject so_casted = (TrackObject) so;
            TrackTimeseries to = null;//(TrackTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            List<TrackTimeseries>lst = timeseriesBox.getAll();
            for(TrackTimeseries ts: lst){
                if(ts.getTimestamp() == getTimestampDay(so_casted.getTimestamp())){
                    to = ts;
                }
            }
            if(to == null){
                to = new TrackTimeseries(getTimestampDay(so_casted.getTimestamp()));
                to.setCurrent(true);
                for(TrackTimeseries ts: lst){
                    ts.setCurrent(false);
                    timeseriesBox.put(ts);
                }
            }
            /*objectBox.put(so_casted);
            ToMany<TrackObject> tmo = to.getValues();
            tmo.add(so_casted);*/
            to.values.add(so_casted);
            timeseriesBox.put(to);
            Log.d(TAG, "Track");
        }
    }

    public void deleteSensorTimeseries(SensorTimeseries st){
        //GLocation
        if(st.getClass().getName().equals(GLocationTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(GLocationsObject.class);
            GLocationTimeseries timeseries = (GLocationTimeseries) st;
            for(GLocationsObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Network
        if(st.getClass().getName().equals(NetworkTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(NetworkTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(NetworkObject.class);
            NetworkTimeseries timeseries = (NetworkTimeseries) st;
            for(NetworkObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Activity
        if(st.getClass().getName().equals(ActivityTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(ActivityTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(ActivityObject.class);
            ActivityTimeseries timeseries = (ActivityTimeseries) st;
            for(ActivityObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //RunningApplication
        if(st.getClass().getName().equals(RunningApplicationTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(RunningApplicationTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(RunningApplicationObject.class);
            RunningApplicationTimeseries timeseries = (RunningApplicationTimeseries) st;
            for(RunningApplicationObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //ScreenOn
        if(st.getClass().getName().equals(ScreenOnTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(ScreenOnTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(ScreenOnObject.class);
            ScreenOnTimeseries timeseries = (ScreenOnTimeseries) st;
            for(ScreenOnObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Track
        if(st.getClass().getName().equals(TrackTimeseries.class.getName())){
            Box timeseriesBox = Module.getBoxStore().boxFor(TrackTimeseries.class);
            Box objectBox = Module.getBoxStore().boxFor(TrackObject.class);
            TrackTimeseries timeseries = (TrackTimeseries) st;
            for(TrackObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
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

    public List<GLocationTimeseries> getGLocationTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
        List<GLocationTimeseries> gLocationTimeseriesList = timeseriesBox.getAll();
        List<GLocationTimeseries> gLocationTimeseriesListResult = new ArrayList<>();
        for(GLocationTimeseries gLocationTimeseries: gLocationTimeseriesList){

            if(onlyNonUpdated && !gLocationTimeseries.isUploaded()){
                if(gLocationTimeseries.getTimestamp_day().equals(timestampDay)){
                    gLocationTimeseriesListResult.add(gLocationTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(gLocationTimeseries.getTimestamp_day().equals(timestampDay)){
                    gLocationTimeseriesListResult.add(gLocationTimeseries);
                }
            }
        }
        return gLocationTimeseriesListResult;
    }

    public List<ActivityTimeseries> getActivityTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(ActivityTimeseries.class);
        List<ActivityTimeseries> activityTimeseriesList = timeseriesBox.getAll();
        List<ActivityTimeseries> activityTimeseriesListResult = new ArrayList<>();
        for(ActivityTimeseries activityTimeseries: activityTimeseriesList){

            if(onlyNonUpdated && !activityTimeseries.isUploaded()){
                if(activityTimeseries.getTimestamp_day().equals(timestampDay)){
                    activityTimeseriesListResult.add(activityTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(activityTimeseries.getTimestamp_day().equals(timestampDay)){
                    activityTimeseriesListResult.add(activityTimeseries);
                }
            }
        }
        return activityTimeseriesListResult;
    }

    public List<NetworkTimeseries> getNetworkTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(NetworkTimeseries.class);
        List<NetworkTimeseries> networkTimeseriesList = timeseriesBox.getAll();
        List<NetworkTimeseries> networkTimeseriesListResult = new ArrayList<>();
        for(NetworkTimeseries networkTimeseries: networkTimeseriesList){

            if(onlyNonUpdated && !networkTimeseries.isUploaded()){
                if(networkTimeseries.getTimestamp_day().equals(timestampDay)){
                    networkTimeseriesListResult.add(networkTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(networkTimeseries.getTimestamp_day().equals(timestampDay)){
                    networkTimeseriesListResult.add(networkTimeseries);
                }
            }
        }
        return networkTimeseriesListResult;
    }

    public List<RunningApplicationTimeseries> getRunningApplicationTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(RunningApplicationTimeseries.class);
        List<RunningApplicationTimeseries> runningApplicationTimeseriesList = timeseriesBox.getAll();
        List<RunningApplicationTimeseries> runningApplicationTimeseriesListResult = new ArrayList<>();
        for(RunningApplicationTimeseries runningApplicationTimeseries: runningApplicationTimeseriesList){

            if(onlyNonUpdated && !runningApplicationTimeseries.isUploaded()){
                if(runningApplicationTimeseries.getTimestamp_day().equals(timestampDay)){
                    runningApplicationTimeseriesListResult.add(runningApplicationTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(runningApplicationTimeseries.getTimestamp_day().equals(timestampDay)){
                    runningApplicationTimeseriesListResult.add(runningApplicationTimeseries);
                }
            }
        }
        return runningApplicationTimeseriesListResult;
    }

    public List<ScreenOnTimeseries> getScreenOnTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(ScreenOnTimeseries.class);
        List<ScreenOnTimeseries> screenOnTimeseriesList = timeseriesBox.getAll();
        List<ScreenOnTimeseries> screenOnTimeseriesListResult = new ArrayList<>();
        for(ScreenOnTimeseries screenOnTimeseries: screenOnTimeseriesList){

            if(onlyNonUpdated && !screenOnTimeseries.isUploaded()){
                if(screenOnTimeseries.getTimestamp_day().equals(timestampDay)){
                    screenOnTimeseriesListResult.add(screenOnTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(screenOnTimeseries.getTimestamp_day().equals(timestampDay)){
                    screenOnTimeseriesListResult.add(screenOnTimeseries);
                }
            }
        }
        return screenOnTimeseriesListResult;
    }

    public List<TrackTimeseries> getTrackTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = Module.getBoxStore().boxFor(TrackTimeseries.class);
        List<TrackTimeseries> trackTimeseriesList = timeseriesBox.getAll();
        List<TrackTimeseries> trackTimeseriesListResult = new ArrayList<>();
        for(TrackTimeseries trackTimeseries: trackTimeseriesList){

            if(onlyNonUpdated && !trackTimeseries.isUploaded()){
                if(trackTimeseries.getTimestamp_day().equals(timestampDay)){
                    trackTimeseriesListResult.add(trackTimeseries);
                }
            }else if(!onlyNonUpdated){
                if(trackTimeseries.getTimestamp_day().equals(timestampDay)){
                    trackTimeseriesListResult.add(trackTimeseries);
                }
            }
        }
        return trackTimeseriesListResult;
    }

    public List<GLocationTimeseries> getGLocationTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
        List<GLocationTimeseries> gLocationTimeseriesList = timeseriesBox.getAll();
        List<GLocationTimeseries> gLocationTimeseriesListResult = new ArrayList<>();
        for(GLocationTimeseries gLocationTimeseries: gLocationTimeseriesList){
            if(!gLocationTimeseries.isUploaded()&& !gLocationTimeseries.isCurrent()){
                gLocationTimeseriesListResult.add(gLocationTimeseries);
            }
        }
        return gLocationTimeseriesListResult;
    }

    public List<ActivityTimeseries> getActivityTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(ActivityTimeseries.class);
        List<ActivityTimeseries> activityTimeseriesList = timeseriesBox.getAll();
        List<ActivityTimeseries> activityTimeseriesListResult = new ArrayList<>();
        for(ActivityTimeseries activityTimeseries: activityTimeseriesList){
            if(!activityTimeseries.isUploaded() && !activityTimeseries.isCurrent()){
                activityTimeseriesListResult.add(activityTimeseries);
            }
        }
        return activityTimeseriesListResult;
    }

    public List<NetworkTimeseries> getNetworkTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(NetworkTimeseries.class);
        List<NetworkTimeseries> networkTimeseriesList = timeseriesBox.getAll();
        List<NetworkTimeseries> networkTimeseriesListResult = new ArrayList<>();
        for(NetworkTimeseries networkTimeseries: networkTimeseriesList){
            if(!networkTimeseries.isUploaded()  && !networkTimeseries.isCurrent()){
                networkTimeseriesListResult.add(networkTimeseries);
            }
        }
        return networkTimeseriesListResult;
    }

    public List<RunningApplicationTimeseries> getRunningApplicationTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(RunningApplicationTimeseries.class);
        List<RunningApplicationTimeseries> runningApplicationTimeseriesList = timeseriesBox.getAll();
        List<RunningApplicationTimeseries> runningApplicationTimeseriesListResult = new ArrayList<>();
        for(RunningApplicationTimeseries runningApplicationTimeseries: runningApplicationTimeseriesList){
            if(!runningApplicationTimeseries.isUploaded() && !runningApplicationTimeseries.isCurrent()){
                runningApplicationTimeseriesListResult.add(runningApplicationTimeseries);
            }
        }
        return runningApplicationTimeseriesListResult;
    }

    public List<ScreenOnTimeseries> getScreenOnTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(ScreenOnTimeseries.class);
        List<ScreenOnTimeseries> screenOnTimeseriesList = timeseriesBox.getAll();
        List<ScreenOnTimeseries> screenOnTimeseriesListResult = new ArrayList<>();
        for(ScreenOnTimeseries screenOnTimeseries: screenOnTimeseriesList){
            if(!screenOnTimeseries.isUploaded()&& !screenOnTimeseries.isCurrent()){
                screenOnTimeseriesListResult.add(screenOnTimeseries);
            }
        }
        return screenOnTimeseriesListResult;
    }

    public List<TrackTimeseries> getTrackTimeseriesNonUpdated(){
        Box timeseriesBox = Module.getBoxStore().boxFor(TrackTimeseries.class);
        List<TrackTimeseries> trackTimeseriesList = timeseriesBox.getAll();
        List<TrackTimeseries> trackTimeseriesListResult = new ArrayList<>();
        for(TrackTimeseries trackTimeseries: trackTimeseriesList){
            if(!trackTimeseries.isUploaded() && !trackTimeseries.isCurrent() ){
                trackTimeseriesListResult.add(trackTimeseries);
            }
        }
        return trackTimeseriesListResult;
    }

    public void deleteAllSensorTimeseriesOlder(long time) {
        //GLocation
        Box gLocBox = Module.getBoxStore().boxFor(GLocationTimeseries.class);
        List<GLocationTimeseries> gLocList = gLocBox.getAll();
        for(GLocationTimeseries gLoc: gLocList){
            if(gLoc.getTimestamp() < time){
                deleteSensorTimeseries(gLoc);
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
        }

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
