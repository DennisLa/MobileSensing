package de.dennis.mobilesensing_module.mobilesensing.Storage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ActivityListener.ActivityObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ActivityListener.ActivityTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocationListener.GLocationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocationListener.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.NetworkListener.NetworkObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.NetworkListener.NetworkTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplicationListener.RunningApplicationObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplicationListener.RunningApplicationTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOnListener.ScreenOnObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOnListener.ScreenOnTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.TrackListener.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.TrackListener.TrackTimeseries;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.relation.ToMany;

/**
 * Created by Dennis on 01.10.2017.
 */

public class ObjectBoxAdapter {

    public void saveSensorObject(SensorObject so){
        //GLocation
        if(so.getClass().getName().equals(GLocationsObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(GLocationTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(GLocationsObject.class);
            GLocationsObject so_casted = (GLocationsObject) so;
            GLocationTimeseries to = (GLocationTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new GLocationTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<GLocationsObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
        //Network
        if(so.getClass().getName().equals(NetworkObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(NetworkTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(NetworkObject.class);
            NetworkObject so_casted = (NetworkObject) so;
            NetworkTimeseries to = (NetworkTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new NetworkTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<NetworkObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
        //Activity
        if(so.getClass().getName().equals(ActivityObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(ActivityTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(ActivityObject.class);
            ActivityObject so_casted = (ActivityObject) so;
            ActivityTimeseries to = (ActivityTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new ActivityTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<ActivityObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
        //RunningApplication
        if(so.getClass().getName().equals(RunningApplicationObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(RunningApplicationTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(RunningApplicationObject.class);
            RunningApplicationObject so_casted = (RunningApplicationObject) so;
            RunningApplicationTimeseries to = (RunningApplicationTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new RunningApplicationTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<RunningApplicationObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
        //ScreenOn
        if(so.getClass().getName().equals(ScreenOnObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(ScreenOnTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(ScreenOnObject.class);
            ScreenOnObject so_casted = (ScreenOnObject) so;
            ScreenOnTimeseries to = (ScreenOnTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new ScreenOnTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<ScreenOnObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
        //Track
        if(so.getClass().getName().equals(TrackObject.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(TrackTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(TrackObject.class);
            TrackObject so_casted = (TrackObject) so;
            TrackTimeseries to = (TrackTimeseries) timeseriesBox.get(getTimestampDay(so_casted.getTimestamp()));
            if(to == null){
                to = new TrackTimeseries(getTimestampDay(so_casted.getTimestamp()));
            }
            objectBox.put(so_casted);
            ToMany<TrackObject> tmo = to.getValues();
            tmo.add(so_casted);
            to.setValues(tmo);
            timeseriesBox.put(to);
        }
    }

    public void deleteSensorTimeseries(SensorTimeseries st){
        //GLocation
        if(st.getClass().getName().equals(GLocationTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(GLocationTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(GLocationsObject.class);
            GLocationTimeseries timeseries = (GLocationTimeseries) st;
            for(GLocationsObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Network
        if(st.getClass().getName().equals(NetworkTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(NetworkTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(NetworkObject.class);
            NetworkTimeseries timeseries = (NetworkTimeseries) st;
            for(NetworkObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Activity
        if(st.getClass().getName().equals(ActivityTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(ActivityTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(ActivityObject.class);
            ActivityTimeseries timeseries = (ActivityTimeseries) st;
            for(ActivityObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //RunningApplication
        if(st.getClass().getName().equals(RunningApplicationTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(RunningApplicationTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(RunningApplicationObject.class);
            RunningApplicationTimeseries timeseries = (RunningApplicationTimeseries) st;
            for(RunningApplicationObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //ScreenOn
        if(st.getClass().getName().equals(ScreenOnTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(ScreenOnTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(ScreenOnObject.class);
            ScreenOnTimeseries timeseries = (ScreenOnTimeseries) st;
            for(ScreenOnObject so:timeseries.getValues()){
                objectBox.remove(so);
            }
            timeseriesBox.remove(st);
        }
        //Track
        if(st.getClass().getName().equals(TrackTimeseries.class.getName())){
            Box timeseriesBox = BoxStore.getDefault().boxFor(TrackTimeseries.class);
            Box objectBox = BoxStore.getDefault().boxFor(TrackObject.class);
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
            Box timeseriesBox = BoxStore.getDefault().boxFor(st.getClass());
            timeseriesBox.put(st);
    }

    public void updateSensorObject(SensorObject so){
        Box objectBox = BoxStore.getDefault().boxFor(so.getClass());
        objectBox.put(so);
    }

    public List<GLocationTimeseries> getGLocationTimeseries(String timestampDay, boolean onlyNonUpdated){
        Box timeseriesBox = BoxStore.getDefault().boxFor(GLocationTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(ActivityTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(NetworkTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(RunningApplicationTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(ScreenOnTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(TrackTimeseries.class);
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
        Box timeseriesBox = BoxStore.getDefault().boxFor(GLocationTimeseries.class);
        List<GLocationTimeseries> gLocationTimeseriesList = timeseriesBox.getAll();
        List<GLocationTimeseries> gLocationTimeseriesListResult = new ArrayList<>();
        for(GLocationTimeseries gLocationTimeseries: gLocationTimeseriesList){
            if(!gLocationTimeseries.isUploaded()){
                gLocationTimeseriesListResult.add(gLocationTimeseries);
            }
        }
        return gLocationTimeseriesListResult;
    }

    public List<ActivityTimeseries> getActivityTimeseriesNonUpdated(){
        Box timeseriesBox = BoxStore.getDefault().boxFor(ActivityTimeseries.class);
        List<ActivityTimeseries> activityTimeseriesList = timeseriesBox.getAll();
        List<ActivityTimeseries> activityTimeseriesListResult = new ArrayList<>();
        for(ActivityTimeseries activityTimeseries: activityTimeseriesList){
            if(!activityTimeseries.isUploaded()){
                activityTimeseriesListResult.add(activityTimeseries);
            }
        }
        return activityTimeseriesListResult;
    }

    public List<NetworkTimeseries> getNetworkTimeseriesNonUpdated(){
        Box timeseriesBox = BoxStore.getDefault().boxFor(NetworkTimeseries.class);
        List<NetworkTimeseries> networkTimeseriesList = timeseriesBox.getAll();
        List<NetworkTimeseries> networkTimeseriesListResult = new ArrayList<>();
        for(NetworkTimeseries networkTimeseries: networkTimeseriesList){
            if(!networkTimeseries.isUploaded()){
                networkTimeseriesListResult.add(networkTimeseries);
            }
        }
        return networkTimeseriesListResult;
    }

    public List<RunningApplicationTimeseries> getRunningApplicationTimeseriesNonUpdated(){
        Box timeseriesBox = BoxStore.getDefault().boxFor(RunningApplicationTimeseries.class);
        List<RunningApplicationTimeseries> runningApplicationTimeseriesList = timeseriesBox.getAll();
        List<RunningApplicationTimeseries> runningApplicationTimeseriesListResult = new ArrayList<>();
        for(RunningApplicationTimeseries runningApplicationTimeseries: runningApplicationTimeseriesList){
            if(!runningApplicationTimeseries.isUploaded()){
                runningApplicationTimeseriesListResult.add(runningApplicationTimeseries);
            }
        }
        return runningApplicationTimeseriesListResult;
    }

    public List<ScreenOnTimeseries> getScreenOnTimeseriesNonUpdated(){
        Box timeseriesBox = BoxStore.getDefault().boxFor(ScreenOnTimeseries.class);
        List<ScreenOnTimeseries> screenOnTimeseriesList = timeseriesBox.getAll();
        List<ScreenOnTimeseries> screenOnTimeseriesListResult = new ArrayList<>();
        for(ScreenOnTimeseries screenOnTimeseries: screenOnTimeseriesList){
            if(!screenOnTimeseries.isUploaded()){
                screenOnTimeseriesListResult.add(screenOnTimeseries);
            }
        }
        return screenOnTimeseriesListResult;
    }

    public List<TrackTimeseries> getTrackTimeseriesNonUpdated(){
        Box timeseriesBox = BoxStore.getDefault().boxFor(TrackTimeseries.class);
        List<TrackTimeseries> trackTimeseriesList = timeseriesBox.getAll();
        List<TrackTimeseries> trackTimeseriesListResult = new ArrayList<>();
        for(TrackTimeseries trackTimeseries: trackTimeseriesList){
            if(!trackTimeseries.isUploaded()){
                trackTimeseriesListResult.add(trackTimeseries);
            }
        }
        return trackTimeseriesListResult;
    }
}
