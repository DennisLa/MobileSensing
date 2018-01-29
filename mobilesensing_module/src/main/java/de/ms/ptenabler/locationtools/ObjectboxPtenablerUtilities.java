package de.ms.ptenabler.locationtools;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.ms.ptenabler.Message.ClusterMessage;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Point;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 14.11.2017.
 */

public class ObjectboxPtenablerUtilities {

    public static void checkLocationInCluster(GLocationsObject gLocationsObject){
        Location loc = new Location(LocationType.COORD,gLocationsObject.getId()+"",new Point((int)(gLocationsObject.getLat()*1000000), (int) (gLocationsObject.getLng()*1000000)));
        ArrayList<ClusteredLocation> clocs = (ArrayList<ClusteredLocation>)ClusterManagement.getManager().getClusteredLocationsFromCache(false);
        ClusteredLocation newCluster=null;
        for(ClusteredLocation cl:clocs){
            UserLocation toTest = new UserLocation(loc,gLocationsObject.timestamp,cl.getId());
            if(ClusterManagement.getManager().coversLocation(cl.getMeta().getHull(),toTest)){
                newCluster=cl;
                break;
            }
        }
        ClusterMessage cm= EventBus.getDefault().getStickyEvent(ClusterMessage.class);
        if(newCluster !=null){
            UserLocation toTest = new UserLocation(loc,gLocationsObject.timestamp,newCluster.getId());
            if(cm!=null && cm.cloc.getId() == newCluster.getId()){
                EventBus.getDefault().postSticky(new ClusterMessage(newCluster, ClusterMessage.STATE.INSIDE,toTest));
            }else{
                EventBus.getDefault().postSticky(new ClusterMessage(newCluster, ClusterMessage.STATE.ENTERING,toTest));
            }
        }else{
            if(cm!=null){
                UserLocation toTest = new UserLocation(loc,gLocationsObject.timestamp,cm.cloc.getId());
                EventBus.getDefault().postSticky(new ClusterMessage(cm.cloc, ClusterMessage.STATE.LEAVING,toTest));
                EventBus.getDefault().removeStickyEvent(ClusterMessage.class);
            }
        }
    }

    public static List<UserLocation> convertGLocationsToUserLoactions(List<GLocationsObject> gLocations){
        ArrayList<UserLocation> locs = new ArrayList<>();
        for(GLocationsObject gloc: gLocations){
            locs.add(convertGLocationToUserLoaction(gloc));
        }
        return locs;
    }

    public static List<ClusteredLocation> convertClustersToClusteredLocations(List<ClusterObject> clusters) {
        ArrayList<ClusteredLocation> clusterLocs = new ArrayList<>();
        for(ClusterObject co: clusters){
            clusterLocs.add(convertClusterToClusteredLocation(co));
        }
        return clusterLocs;
    }

    public static ClusteredLocation convertClusterToClusteredLocation(ClusterObject cluster){
        //return new ClusteredLocation(new Location(LocationType.COORD,cluster.getId()+"",  new Point((int)cluster.getLat()*1000000,(int) cluster.getLng()*1000000)), cluster.getDate(), cluster.getId(), ClusterMetaData.ClusterType.DAILY_CLUSTER);
        return new ClusteredLocation(new Location(LocationType.COORD,cluster.getId()+"",  new Point((int)cluster.getLat()*1000000,(int) cluster.getLng()*1000000)), cluster.getDate(), cluster.getId(), cluster.count,cluster.firstseen,cluster.metajson);
    }

    public static GLocationsObject convertUserLocationToGLocation(UserLocation userLocation){
        GLocationsObject gloc = new GLocationsObject(userLocation.getID(),userLocation.getDate(),userLocation.getLoc().lat/1000000.0,userLocation.getLoc().lon/1000000.0,0,userLocation.getParentCluster());
        //Get Speed From ObjectBox Before Updating
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        GLocationsObject gloc_old = oba.getGLocationObject(userLocation.getID());
        float speed = 0;
        if(gloc_old != null){
            speed = gloc_old.speed;
        }
        gloc.speed = speed;
        return gloc;
    }

    private static UserLocation convertGLocationToUserLoaction(GLocationsObject gloc) {
        return new UserLocation(new Location(LocationType.COORD,gloc.getId()+"",new Point((int)(gloc.getLat()*1000000), (int) (gloc.getLng()*1000000))),gloc.getTimestamp(),gloc.parentCluster);
    }

    public static void updateSensorObjectAndLocations(ClusteredLocation toAdd, Dataset instances) {
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        //long id = oba.updateSensorObject(new ClusterObject(toAdd.getId(),toAdd.getDate(),toAdd.getFirstseen(),toAdd.getCount(),toAdd.getLoc().lat,toAdd.getLoc().lon, toAdd.getMeta().toString()));
        long id = oba.updateSensorObject(convertClusteredLocationToClusterObject(toAdd));
        //Update Locations
        if(instances != null){
            for(Instance i: instances){
                if(i instanceof UserLocation){
                    GLocationsObject gloc = convertUserLocationToGLocation((UserLocation) i);
                    gloc.parentCluster = id;
                    oba.updateSensorObject(gloc);
                }
            }
        }
}

    public static void updateParentClusterofLocations(long id, long new_id) {
        Box glocationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<GLocationsObject> gLocs = glocationBox.find("parentCluster", id);
        for(GLocationsObject gLoc: gLocs){
            gLoc.parentCluster = new_id;
            glocationBox.put(gLoc);
        }
    }

    public static void mergeCluster(ClusteredLocation toMerge, ClusteredLocation remaining){
        updateParentClusterofLocations(toMerge.getId(),remaining.getId());
        remaining.setCount(Math.max(toMerge.getCount(), remaining.getCount()));
        remaining.setFirstseen(Math.min(toMerge.getFirstseen(), remaining.getFirstseen()));
        remaining.setDate(Math.max(toMerge.getDate(), remaining.getDate()));
        Box clusterBox = Module.getBoxStore().boxFor(ClusterObject.class);
        //updateClusteredLocation(remaining);
        clusterBox.put(convertClusteredLocationToClusterObject(remaining));
        //deleteClusteredLocation(toMerge.getId());
        clusterBox.remove(toMerge.getId());
    }

    private static ClusterObject convertClusteredLocationToClusterObject(ClusteredLocation toMerge) {
        ClusterObject co = new ClusterObject(toMerge.getId(),toMerge.getDate(),toMerge.getFirstseen(),toMerge.getCount(),toMerge.getLoc().lat/1000000.0,toMerge.getLoc().lon/1000000.0, toMerge.getMeta().toString());
        return co;
    }

    public static void saveClusterLocation(Location loc, Dataset instances, ClusterMetaData.ClusterType type) {
        //ClusteredLocation cl =saveClusterLocation(loc, type);
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        //Box clusterBox = Module.getBoxStore().boxFor(ClusterObject.class);
        //long id = clusterBox.put(new ClusterObject(new Date().getTime(), new Date().getTime(),1, loc.lat, loc.lon,new ClusterMetaData(type).toString()));
        long id = oba.updateSensorObject(new ClusterObject(new Date().getTime(), new Date().getTime(),1, loc.lat/1000000.0, loc.lon/1000000.0,new ClusterMetaData(type).toString()));
        //if(id!=null)setClusterIDOfLocations(ds, cl.getId());
        //Update Locations
        for(Instance i: instances){
            if(i instanceof UserLocation){
                GLocationsObject gloc = convertUserLocationToGLocation((UserLocation) i);
                gloc.parentCluster = id;
                oba.updateSensorObject(gloc);
            }
        }

    }

    public static void clearClusteredLocation(int olderThanXDays) {
        Date x = new Date();
        long millis =x.getTime()-((long)olderThanXDays *1000L*3600L*24L);
        Box clusterBox = Module.getBoxStore().boxFor(ClusterObject.class);
        Box gLocBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<ClusterObject> cos = clusterBox.getAll();
        for(ClusterObject co: cos){
            if(co.getDate() < millis){
                clusterBox.remove(co);
                updateParentClusterofLocations(co.getId(),-1);
            }
        }
    }

    public static List<UserLocation> getAllHistoryLocsofCluster(long cid, long start, long end) {
        Box gLocationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<GLocationsObject> gLocs = gLocationBox.find("parentCluster",cid);
        List<UserLocation> locs = new ArrayList<>();
        for(GLocationsObject gLoc: gLocs){
            if(gLoc.getTimestamp() >= start && gLoc.getTimestamp() <= end){
                locs.add(convertGLocationToUserLoaction(gLoc));
            }
        }
        return locs;
    }
    public static List<UserLocation> getAllHistoryLocs(long since, long until, boolean descending){
        Box gLocationBox = Module.getBoxStore().boxFor(GLocationsObject.class);
        List<GLocationsObject> gLocs = gLocationBox.getAll();
        List<UserLocation> locs = new ArrayList<>();
        if(descending){
            for(int i = 0; i < gLocs.size();i++){
                if(gLocs.get(i).getTimestamp() > since && gLocs.get(i).getTimestamp() < until){
                    locs.add(convertGLocationToUserLoaction(gLocs.get(i)));
                }
            }
        }else{
            for(int i = gLocs.size()-1; i >=0;i--){
                if(gLocs.get(i).getTimestamp() > since && gLocs.get(i).getTimestamp() < until){
                    locs.add(convertGLocationToUserLoaction(gLocs.get(i)));
                }
            }
        }
        return locs;
    }

    public static double[] getLocationLatLng() {
        SharedPreferences prefs  = Module.getContext().getSharedPreferences("LastLocation", Context.MODE_PRIVATE);
        double arr[] = new double[2];
        arr[0] = prefs.getFloat("LastLat",3.5000000f);
        arr[1] = prefs.getFloat("LastLng",4.0000000f);
        return arr;
    }

    public static Location getLocation() {
        SharedPreferences prefs  = Module.getContext().getSharedPreferences("LastLocation", Context.MODE_PRIVATE);
        int lat = prefs.getInt("LastLat", 35000000);
        int lon = prefs.getInt("LastLng",40000000);
        return Location.coord(lat,lon);
    }
}
