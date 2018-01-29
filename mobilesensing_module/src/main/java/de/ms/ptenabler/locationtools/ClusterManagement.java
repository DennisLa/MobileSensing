package de.ms.ptenabler.locationtools;



import android.content.SharedPreferences;

import android.util.Log;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.OperatorIntersects;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;

import com.google.android.gms.maps.model.LatLng;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;



import net.sf.javaml.clustering.DensityBasedSpatialClustering;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster.ClusterObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;
import de.ms.ptenabler.locationtools.convexhull.GrahamScan;
import de.ms.ptenabler.locationtools.convexhull.Point2D;
import de.ms.ptenabler.locationtools.presenceprobability.TimeValueMatrix;
import de.ms.ptenabler.locationtools.trajectory.Trajectory;
import de.ms.ptenabler.locationtools.trajectory.TrajectoryElement;
import de.ms.ptenabler.locationtools.trajectory.TrajectoryNode;
import de.ms.ptenabler.locationtools.trajectory.TrajectoryPath;
import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Martin on 05.03.2015.
 */
public class ClusterManagement {
    private static ClusterManagement manager;
    private  int minLocs = 10;
    private  int distance4Clustering = 25;
    private  Dataset[] resultsCluster;
    private  HashMap<Long, HashMap<Long, Double>> probresult = null;
    private  HashMap<Long, HashMap<Long, Integer>> sums;
    private  int slicesOfDay;
    private SharedPreferences  prefs;
    private static final String TIME_DISTRIBUTION_PREFERENCE = "timedistribution";
    private static final String TIME_DISTRIBUTION_BASE_PREFERENCE = "timedistributionbase";
    private ClusterManagement(){
        //slicesOfDay = Utilities.getContext().getResources().getInteger(R.integer.slicesOfDay);
        slicesOfDay = 96;
       // prefs = Utilities.getPeference();
        prefs = Module.getContext().getSharedPreferences("Clustering",MODE_PRIVATE);
    }
    public static ClusterManagement getManager(){
        if(manager!=null) return manager;
        return new ClusterManagement();
    }
    public  HashMap<Location, Dataset> clusterLocations(Date since, Date until, Integer max, boolean onlyUnClustered) {
        HashMap<Location, Dataset> clusterCenters = new HashMap<Location, Dataset>();

        Log.d("PTEnabler", "Initializing Locations for clustering\nSince:" + since.toLocaleString() + "\nUntil:" + until.toLocaleString());
        List<UserLocation> locs;
        List<GLocationsObject> objectboxLocations;
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        if (onlyUnClustered) {
            objectboxLocations = oba.getUnclusteredGLocations(since.getTime(),until.getTime());
            locs = ObjectboxPtenablerUtilities.convertGLocationsToUserLoactions(objectboxLocations);
            //locs = Utilities.openDBConnection().getUnclusteredHistoryLocs(since.getTime(), until.getTime());
        } else {
            objectboxLocations = oba.getGLocationTimeseries(since.getTime(),until.getTime());
            locs = ObjectboxPtenablerUtilities.convertGLocationsToUserLoactions(objectboxLocations);
            //locs = Utilities.openDBConnection().getAllHistoryLocs(since.getTime(), until.getTime());
        }

        Log.d("PTEnabler", "Number of Locations available for clustering:" + locs.size());


        resultsCluster = clusterData(max, locs);


        Log.d("PTEnabler", "Finished Clustering");
        for (Dataset y : resultsCluster) {
            double sumLat = 0.0;
            double sumLon = 0.0;
            Iterator<Instance> it = y.listIterator();
            while (it.hasNext()) {
                Instance i = it.next();
                if (i instanceof UserLocation) {
                    UserLocation uloc = (UserLocation) i;
                    sumLat += ((double) uloc.getLoc().lat) / 1000000.0;
                    sumLon += ((double) uloc.getLoc().lon) / 1000000.0;
                }


            }
            int lat = (int) ((sumLat / (double) y.size()) * 1000000.0);
            int lon = (int) ((sumLon / (double) y.size()) * 1000000.0);
            clusterCenters.put(Location.coord(lat, lon), y);
        }

        return clusterCenters;
    }

    private Dataset[] clusterData(Integer max, List<UserLocation> locs){
        Dataset data = new DefaultDataset();
        if (max != null) {
            int allLocs = locs.size();
            int every = (allLocs > max) ? allLocs / max : 1;
            int i = 0;
            for (UserLocation loc : locs) {
                if ((i++) % every == 0) {
                    data.add(loc);
                } else {
                    continue;
                }
            }
        } else {
            for (UserLocation loc : locs) {
//					double[] x= new double[]{((double)(loc.getLoc().lat))/1000000,((double)(loc.getLoc().lon))/1000000};
//					Instance instance = new DenseInstance(x);
                data.add(loc);
            }
        }

        Log.d("PTEnabler", "" + data.size() + " Locations initialized for clustering");
        Log.d("PTEnabler", "" + minLocs + " Locations required for clustering");
        DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(distance4Clustering, minLocs, new MyDistanceMeasure());
        Log.d("PTEnabler", "Starting Clustering");
        return clusterer.cluster(data);
    }

    public  HashMap<Location, Dataset> clusterLocations(Date since, Date until, Integer max) {
        return clusterLocations(since, until, max, false);
    }

    public  List<ClusteredLocation> getClusteredLocationsFromCache(boolean includeDeleted) {

        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        List<ClusterObject> clusters= oba.getAllClusterObjects();
        List<ClusteredLocation> locs = ObjectboxPtenablerUtilities.convertClustersToClusteredLocations(clusters);
        //List<ClusteredLocation> locs=Utilities.openDBConnection().getAllClusterLocs();
        if(!includeDeleted){
            Iterator<ClusteredLocation> it = locs.iterator();
            while(it.hasNext()){
                ClusteredLocation loc = it.next();
                if(loc.getMeta().deleted){
                    it.remove();
                }
            }
        }

        return locs;
    }
    public  List<ClusteredLocation> getClusteredLocationWithIDs(Collection<Long> ids){
        List<ClusteredLocation> res = getClusteredLocationsFromCache(true);
        Iterator<ClusteredLocation> it = res.iterator();
        while(it.hasNext()){
            ClusteredLocation cloc = it.next();
            if(!ids.contains(cloc.getId())){
                it.remove();
            }
        }
    return res;
    }

    public  List<ClusteredLocation> getCloseByClusteredLocationsFromCache(double distanceInMeter) {
        List<ClusteredLocation> all = getClusteredLocationsFromCache(false);
        Iterator<ClusteredLocation> it = all.iterator();
        while (it.hasNext()) {
            ClusteredLocation cl = it.next();
            //double[] currentLoc = PTNMELocationManager.getManager().getLocationLatLng();
            double[] currentLoc = ObjectboxPtenablerUtilities.getLocationLatLng();
            if (currentLoc != null) {
                double distance = PTNMELocationManager.getManager().computeDistance(cl.getLoc(), currentLoc[0], currentLoc[1]);
                if (distance > distanceInMeter || distance < distance4Clustering) {
                    it.remove();
                }
            }

        }
        return all;

    }

    public  ClusteredLocation getClosestClusteredLocationsFromCache(boolean includeDeleted) {
        List<ClusteredLocation> all = getClusteredLocationsFromCache(includeDeleted);
        ClusteredLocation res = null;
        //double[] locations = PTNMELocationManager.getManager().getLocationLatLng();
        double[] locations = ObjectboxPtenablerUtilities.getLocationLatLng();
        if (locations != null) {
            //double x = PTNMELocationManager.getManager().getLocationLatLng()[0];
            //double y = PTNMELocationManager.getManager().getLocationLatLng()[1];
            double x = ObjectboxPtenablerUtilities.getLocationLatLng()[0];
            double y = ObjectboxPtenablerUtilities.getLocationLatLng()[1];
            double mindistance = Double.MAX_VALUE;
            Iterator<ClusteredLocation> it = all.iterator();
            while (it.hasNext()) {
                ClusteredLocation cl = it.next();
                double distance = PTNMELocationManager.getManager().computeDistance(cl.getLoc(), x, y);
                if (distance < mindistance) {
                    res = cl;
                    mindistance = distance;
                }
            }
            return res;
        } else {
            return null;
        }

    }

    public  void addNewClusteredLocations(Map<Location, Dataset> locs, ClusterMetaData.ClusterType type) {


        for (Location loc : locs.keySet()) {
            ClusteredLocation toAdd = null;
            List<ClusteredLocation> oldClusteredLocs = getClusteredLocationsFromCache(true);
            List<UserLocation> newHull = ClusterManagement.getManager().getConvextHull(dataset2ULList(locs.get(loc)));
            if(newHull.size()<=3) continue; //AGPS ERROR
            for (ClusteredLocation oldLoc : oldClusteredLocs) {
                List<UserLocation> oldHull = ClusterManagement.getManager().getConvexHullofCluster(oldLoc.getId());
                if (areOverlapping(oldHull,newHull)) {
                    toAdd = oldLoc;
                    break;
                }
            }
            if (toAdd == null) {
                //Utilities.openDBConnection().saveClusterLocation(loc, locs.get(loc), type);
                ObjectboxPtenablerUtilities.saveClusterLocation(loc,locs.get(loc),type);
            } else {
                Log.d("PTEnabler", "Updating Clustered Location: ID " + toAdd.getId() + " Last Clustered: " + new Date(toAdd.getDate()).toLocaleString());
                int lat = (loc.lat + toAdd.getLoc().lat) / 2;
                int lon = (loc.lon + toAdd.getLoc().lon) / 2;
                Location upatedLL;
                if (toAdd.getLoc().place != null) {
                    upatedLL = new Location(LocationType.ADDRESS, toAdd.getLoc().id, lat, lon, toAdd.getLoc().place, toAdd.getLoc().name);
                } else {
                    upatedLL = Location.coord(lat, lon);
                }
                toAdd.setDate(new Date().getTime());
                toAdd.setLoc(upatedLL);
                toAdd.setCount(toAdd.getCount() + 1);
                //Utilities.openDBConnection().updateClusteredLocation(toAdd, locs.get(loc));
                ObjectboxPtenablerUtilities.updateSensorObjectAndLocations(toAdd, locs.get(loc));
            }
        }


    }
    public List<ClusteredLocation> cleanCluster(List<ClusteredLocation> clocs){
        Vector<Long> processed = new Vector<Long>();
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        for(ClusteredLocation cloc1 : clocs){
            if(cloc1.getMeta().getHull().size()<=3){
                //Utilities.openDBConnection().deleteClusteredLocation(cloc1.getId());
                oba.deleteClusterObject(cloc1.getId());
                //Utilities.openDBConnection().updateParentClusterofLocations(cloc1.getId(), 0);
                ObjectboxPtenablerUtilities.updateParentClusterofLocations(cloc1.getId(),-1);
            }
            for(ClusteredLocation cloc2: clocs){
                if(processed.contains(cloc1.getId()) || processed.contains(cloc2.getId())){
                    continue;
                }
                boolean overlap=    ClusterManagement.getManager().areOverlapping(cloc1.getMeta().getHull(), cloc2.getMeta().getHull())||
                                    ClusterManagement.getManager().coversLocation(cloc1.getMeta().getHull(), new UserLocation(cloc2.getLoc(),cloc2.getDate(),cloc2.getId()))||
                                    ClusterManagement.getManager().coversLocation(cloc2.getMeta().getHull(), new UserLocation(cloc1.getLoc(),cloc1.getDate(),cloc1.getId()));
                if(overlap && cloc1.getId()!=cloc2.getId()){
                    Log.d("PTEnabler", "Merging Cluster " + cloc1.getId() + " and " + cloc2.getId());
                    if(cloc2.getCount()<cloc1.getCount()){
                        //Utilities.openDBConnection().mergeCluster(cloc2,cloc1); // cloc1 remains
                        ObjectboxPtenablerUtilities.mergeCluster(cloc2,cloc1);
                    }else{
                        //Utilities.openDBConnection().mergeCluster(cloc1,cloc2); // cloc2 remains
                        ObjectboxPtenablerUtilities.mergeCluster(cloc1,cloc2);
                    }
                    processed.add(cloc1.getId());
                    processed.add(cloc2.getId());
                    //List<UserLocation> tempLocs= Utilities.openDBConnection().getAllHistoryLocsofCluster(cloc1.getId(), 0, new Date().getTime());
                    List<UserLocation> tempLocs = ObjectboxPtenablerUtilities.getAllHistoryLocsofCluster(cloc1.getId(), 0, new Date().getTime());
                    //Utilities.openDBConnection().updateParentClusterofLocations(cloc1.getId(), 0);
                    ObjectboxPtenablerUtilities.updateParentClusterofLocations(cloc1.getId(),-1);
                    Dataset[] res = clusterData(Integer.MAX_VALUE, tempLocs);
                    Dataset relevant = null;
                    for(Dataset x: res){
                        if(relevant==null){
                            relevant = x;
                            continue;
                        }else{
                            if(relevant.size()<x.size()){
                                relevant=x;
                            }
                        }
                    }
                    //Utilities.openDBConnection().updateClusteredLocation(cloc1,relevant);
                    ObjectboxPtenablerUtilities.updateSensorObjectAndLocations(cloc1,relevant);
                }
            }
        }
        return getClusteredLocationsFromCache(true);
    }

    public  void addNewClusteredLocations(List<ClusteredLocation> clocs) {

        for (ClusteredLocation loc : clocs) {
            //Utilities.openDBConnection().updateClusteredLocation(loc, null);
            ObjectboxPtenablerUtilities.updateSensorObjectAndLocations(loc,null);
        }
    }

    public  void clearClusters() {
        clearClusters(14);
    }

    public  void clearClusters(int olderThanXdays) {
        //Utilities.openDBConnection().clearClusteredLocations(olderThanXdays);
        ObjectboxPtenablerUtilities.clearClusteredLocation(olderThanXdays);
    }

    public  Set<PropableNextLocationResult> getProbableDestinations(long cid, boolean includeCurrent) {
        List<ClusteredLocation> clocations = getClusteredLocationsFromCache(includeCurrent);

        probresult = calculateProbabilityForDestinations(loadTrajectorySums());

        HashMap<Long, Double> currentCluster = probresult.get(cid);
        if (currentCluster != null && currentCluster.size() != 0) {
            TreeSet<PropableNextLocationResult> results = new TreeSet<PropableNextLocationResult>();
            for (ClusteredLocation cloc : clocations) {
                if (currentCluster.keySet().contains(cloc.getId())) {
                    //Location currentGeoPos = PTNMELocationManager.getManager().getLocation();
                    Location currentGeoPos = ObjectboxPtenablerUtilities.getLocation();
                    if ((currentGeoPos != null && PTNMELocationManager.getManager().computeDistance(cloc.getLoc(), currentGeoPos) > distance4Clustering) || includeCurrent)
                        results.add(new PropableNextLocationResult(currentCluster.get(cloc.getId()), cloc));
                }
            }
            return results;
        }
        return null;
    }
    public HashMap<Long, HashMap<Long, Integer>> createTrajectorySums(){
        Log.d("PTEnabler", "Creating new Trajectory Sum File");
        Trajectory[] trajects;
        if (sums != null) {
            return sums;
        }else{
            sums = new HashMap<Long, HashMap<Long, Integer>>();
            trajects =getTrajectoriesUntil(new Date().getTime(),365,1000l*60l*5l);
            for(int i=0; i<trajects.length; i++){
                TreeSet<TrajectoryElement> elements=trajects[i].elements;
                Iterator<TrajectoryElement> it = elements.iterator();
                TrajectoryNode last =null;
                while (it.hasNext()){
                    TrajectoryElement toCheck =it.next();
                    if(toCheck instanceof TrajectoryPath){
                        continue;
                    }
                    TrajectoryNode current = (TrajectoryNode)toCheck;
                    if(last==null || (last!=null&&current.nodeLocation.getId()==last.nodeLocation.getId())){
                        last = current;
                        continue;
                    }
                    HashMap<Long, Integer> c1Scores = sums.get(last.nodeLocation.getId());
                    if (c1Scores != null) {
                        Integer c1c2score = c1Scores.get(current.nodeLocation.getId());
                        if (c1c2score == null) {
                            c1Scores.put(current.nodeLocation.getId(), 1);
                        } else {
                            c1Scores.put(current.nodeLocation.getId(), (c1c2score.intValue() + 1));
                        }
                    } else {
                        c1Scores = new HashMap<Long, Integer>();
                        c1Scores.put(current.nodeLocation.getId(), 1);
                        sums.put(last.nodeLocation.getId(), c1Scores);
                    }
                    last = current;
                }
            }
            File file = new File(Module.getContext().getDir("data", MODE_PRIVATE), "sums");
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(sums);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("PTEnabler", "Trajectory File created");
            return sums;
        }

    }

    public HashMap<Long, HashMap<Long, Integer>> loadTrajectorySums(){
        File file = new File(Module.getContext().getDir("data", MODE_PRIVATE), "sums");
        if(!file.exists()){
            createTrajectorySums();
        }
        HashMap<Long, HashMap<Long, Integer>> map;
        try {
            ObjectInputStream inputstream = new ObjectInputStream(new FileInputStream(file));
            map = (HashMap<Long, HashMap<Long, Integer>>) inputstream.readObject();
            inputstream.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void invalidateTrajectorySums(){
        sums = null;
    }

    /**
     *
     * @param start The date which will serve as the End Date of the Interval. Typically TODAY.
     * @param daysBefore The amount of days before Start.
     * @param minDurationThreshold The minimal duration of a Path or V
     * @return
     */
    public  Trajectory[] getTrajectoriesUntil(long start, int daysBefore, long minDurationThreshold){

        Trajectory[] trajects = new Trajectory[daysBefore];
        for(int i= 0; i< daysBefore; i++){
            long startTime = start-(1000l*3600l*24l*(i+1));
            long endTime = start-(1000l*3600l*24l*i);

            trajects[i] = getTrajectory(startTime, endTime).cleanTrajectory(minDurationThreshold);
        }
        return trajects;
    }

    public  Set<PropableNextLocationResult> getProbableDestinations(long cid) {
        return getProbableDestinations(cid, false);
    }

    public  Set<PropableNextLocationResult> getPropableLocationForDate(Date x, boolean includeCurPosCluster) {
        //List<UserLocation> all = Utilities.openDBConnection().getAllHistoryLocs(new Date().getTime()-(1000l*3600l*24l*14l), new Date().getTime());
        List<UserLocation> all = ObjectboxPtenablerUtilities.getAllHistoryLocs(new Date().getTime()-(1000l*3600l*24l*14l), new Date().getTime(),true);
        Calendar toLookFor = Calendar.getInstance();
        toLookFor.setTimeInMillis(x.getTime());
        Calendar loccal = Calendar.getInstance();
        HashMap<Long, Double> props = new HashMap<Long, Double>();
        HashMap<Long, Integer> hourOfDaytoUse = new HashMap<Long, Integer>();
        for (UserLocation loc : all) {
            loccal.setTimeInMillis(loc.getDate());
            if (loc.getParentCluster() != 0
                    && loccal.get(Calendar.HOUR_OF_DAY) == toLookFor.get(Calendar.HOUR_OF_DAY)) {


                if (hourOfDaytoUse.get(loc.getParentCluster()) != null) {
                    if (loccal.get(Calendar.DAY_OF_WEEK) == toLookFor.get(Calendar.DAY_OF_WEEK)) {
                        hourOfDaytoUse.put(loc.getParentCluster(), hourOfDaytoUse.get(loc.getParentCluster()) + 4);
                    } else {
                        hourOfDaytoUse.put(loc.getParentCluster(), hourOfDaytoUse.get(loc.getParentCluster()) + 1);
                    }

                } else {
                    if (loccal.get(Calendar.DAY_OF_WEEK) == toLookFor.get(Calendar.DAY_OF_WEEK)) {
                        hourOfDaytoUse.put(loc.getParentCluster(), 4);
                    } else {
                        hourOfDaytoUse.put(loc.getParentCluster(), 1);
                    }

                }


            }
        }
        double sum = 0;
        for (int count : hourOfDaytoUse.values()) {
            sum += count;
        }

        for (long cid : hourOfDaytoUse.keySet()) {
            props.put(cid, ((double) hourOfDaytoUse.get(cid)) / sum);
        }
        TreeSet<PropableNextLocationResult> res = new TreeSet<PropableNextLocationResult>();
        for (ClusteredLocation cloc : getClusteredLocationsFromCache(false)) {
            if (props.keySet().contains(cloc.getId())) {
                //double[] currentLoc = PTNMELocationManager.getManager().getLocationLatLng();
                double[] currentLoc = ObjectboxPtenablerUtilities.getLocationLatLng();
                double dist = Double.MAX_VALUE;
                if (currentLoc != null) {
                    dist = PTNMELocationManager.getManager().computeDistance(cloc.getLoc(), currentLoc[0], currentLoc[1]);
                }

                if (dist > distance4Clustering || includeCurPosCluster) {
                    res.add(new PropableNextLocationResult(props.get(cloc.getId()), cloc));
                }
            }
        }

        return res;
    }

    public Map<Long,TimeValueMatrix> getTimeProbability(int slicesOfDay, int daysInPast){
        TimeValueMatrix all = new TimeValueMatrix(slicesOfDay);
        long pointInPast = new Date().getTime()-(1000l*3600l*24l*daysInPast);
        //for(UserLocation uloc:Utilities.openDBConnection().getAllHistoryLocs(pointInPast, new Date().getTime())){
        for(UserLocation uloc:ObjectboxPtenablerUtilities.getAllHistoryLocs(pointInPast, new Date().getTime(),true)){
            all.addTimeEvent(uloc.getDate());
        }
        List<ClusteredLocation> clos = getClusteredLocationsFromCache(true);
        HashMap<Long, TimeValueMatrix> res = new HashMap<Long,TimeValueMatrix>();
        for(ClusteredLocation cloc: clos){
            TimeValueMatrix tm = new TimeValueMatrix(slicesOfDay, all.getMatrix());
            //for(UserLocation uloc:Utilities.openDBConnection().getAllHistoryLocsofCluster(cloc.getId(), pointInPast,new Date().getTime())){
            for(UserLocation uloc:ObjectboxPtenablerUtilities.getAllHistoryLocsofCluster(cloc.getId(), pointInPast,new Date().getTime())){
                tm.addTimeEvent(uloc.getDate());
            }
            double[][] resforCloc= new double[7][slicesOfDay];
            //System.out.println(tm);
            res.put(cloc.getId(),tm);
        }
        return res;
    }

    private  HashMap<Long, HashMap<Long, Double>> calculateProbabilityForDestinations(HashMap<Long, HashMap<Long, Integer>> inputs) {
        HashMap<Long, HashMap<Long, Double>> res = new HashMap<Long, HashMap<Long, Double>>();
        for (long c1 : inputs.keySet()) {
            HashMap<Long, Integer> c1scores = inputs.get(c1);
            double sum = 0;
            for (int count : c1scores.values()) {
                sum += count;
            }
            for (long c1c2 : c1scores.keySet()) {
                HashMap<Long, Double> c1c2prob = res.get(c1);
                if (c1c2prob == null) c1c2prob = new HashMap<Long, Double>();
                if (sum != 0) c1c2prob.put(c1c2, ((double) c1scores.get(c1c2)) / sum);
                res.put(c1, c1c2prob);
            }
        }
        return res;
    }

    public  String exportClusters(long start){
        Gson gson = new Gson();
        ClusterReport report = ClusterReport.generateReport(false,start);
        String test = gson.toJson(report);
        return test;

    }
    public void updateTimeDistribution(){
        Gson gson = new Gson();
        Map<Long,TimeValueMatrix> dist = createTimeDistribution();
        Type collectionType = new TypeToken<Map<Long,TimeValueMatrix>>(){}.getType();
        prefs.edit().putString(TIME_DISTRIBUTION_PREFERENCE, gson.toJson(dist,collectionType)).commit();

    }
    public Map<Long, TimeValueMatrix> getTimeDistribution(){
        Gson gson  = new Gson();
        Type collectionType = new TypeToken<Map<Long,TimeValueMatrix>>(){}.getType();
        HashMap<Long, TimeValueMatrix> map = gson.fromJson(prefs.getString(TIME_DISTRIBUTION_PREFERENCE,""),collectionType);
        return map;
    }
    public Map<Long,TimeValueMatrix> createTimeDistribution(){
        int timeframe;
        try{
            timeframe = Integer.parseInt(prefs.getString("timeframekey","14"));
        }catch (NumberFormatException e){
            timeframe = 14;
        }
        Map<Long,TimeValueMatrix> distribution = getTimeProbability(slicesOfDay,timeframe);
        return distribution;
    }
    public String exportTimeDistribution(){
        Gson gson = new Gson();

        Map<Long,TimeValueMatrix> map = getTimeDistribution();
        HashMap<String, Object>[] distributions = new HashMap[map.size()];
        int i=0;
        for(Long currentKey:map.keySet()){
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("cluster",""+currentKey);
            temp.put("distribution",map.get(currentKey).getBasedMatrix());
            distributions[i++] = temp;
        }
    return gson.toJson(distributions);
    }

    public  Trajectory getTrajectory(long start, long end){
        Trajectory result = new Trajectory();
        List<ClusteredLocation> clocations = getClusteredLocationsFromCache(false);
        HashMap<Integer, ClusteredLocation> probLoc = new HashMap<Integer, ClusteredLocation>();
        TreeSet<UserLocation> ulocs = new TreeSet<UserLocation>();
        //ulocs.addAll(Utilities.openDBConnection().getAllHistoryLocs(start, end, false));
        ulocs.addAll(ObjectboxPtenablerUtilities.getAllHistoryLocs(start,end, false));
        HashMap<Long, HashMap<Long, Integer>> sums = new HashMap<Long, HashMap<Long, Integer>>();
        Iterator<UserLocation> it = ulocs.iterator();
        UserLocation previousLocation = null;
        int tempCount = 0;
        TrajectoryPath path = null;
        TrajectoryNode node=null;
        while (it.hasNext()) {
            UserLocation current = it.next();
            if (previousLocation != null) {
                // Sind nicht mehr bei der ersten Location
                if (previousLocation.getParentCluster() != current.getParentCluster()) {
                    // Wechsel von Cluster auf Weg oder andersherum oder von Cluster zu anderem Cluster
                    if (previousLocation.getParentCluster() > 0) {
                        //Wechsel von Cluster->?: Alten Cluster als Node hinzufügen
                        result.addElement(node);
                        if (current.getParentCluster() > 0) {
                            //Nachfolger von Cluster ist auch Cluster: node neu initialisieren
                            ClusteredLocation cl = getClusteredLocation(current.getParentCluster());
                            if (cl == null || cl.getMeta().deleted) {
                                continue;
                            }
                            // Parent cluster existiert und ist nicht gelöscht
                                node = new TrajectoryNode(cl);
                                node.addLocationSample(current);

                        } else {
                            //Nachfolger ist Path: Patch initialsieren
                            path = new TrajectoryPath();
                            path.addElement(current);
                        }

                    } else {
                        //Wechsel von Path in Cluster: Path als Element hinzugügen
                        ClusteredLocation cl = getClusteredLocation(current.getParentCluster());
                        if (cl == null || cl.getMeta().deleted) {
                            continue;
                        }
                        result.addElement(path);
                        // Parent cluster existiert und ist nicht gelöscht
                        node = new TrajectoryNode(cl);
                        node.addLocationSample(current);
                    }
                } else {
                    // Bleiben in aktuellen Cluster oder in Path
                    if (previousLocation.getParentCluster() > 0) {
                        //Bleiben in Cluster: Sample hinzufügen
                        if(node==null){
                            Log.d("PTEnabler", "Adding Sample for Cluster " + current.getParentCluster());
                        }
                        node.addLocationSample(current);
                    } else {
                        //Bleiben auf Path: Sample zu Strecke hinzufügen
                        path.addElement(current);
                    }
                }

            } else {
                //Erste Location
                if(current.getParentCluster()>0){
                    //Erste Location gehört zu Cluster
                    ClusteredLocation cl = getClusteredLocation(current.getParentCluster());
                    if (cl == null || cl.getMeta().deleted) {
                        continue;

                    }
                    // Parent cluster existiert
                    node = new TrajectoryNode(cl);
                    node.addLocationSample(current);
                }else{
                    path = new TrajectoryPath();
                    path.addElement(current);
                }
            }


        previousLocation = current;
        }
        if(previousLocation!=null && previousLocation.getParentCluster()>0){
            if(node!=null) result.addElement(node);
        }else{
            if(path!=null) result.addElement(path);
        }
        return result;
    }

    public  List<UserLocation> getConvexHullofCluster(long id){
        //List<UserLocation> locs = Utilities.openDBConnection().getAllHistoryLocsofCluster(id,0,new Date().getTime());
        List<UserLocation> locs = ObjectboxPtenablerUtilities.getAllHistoryLocsofCluster(id, 0,new Date().getTime());
        if(locs.size()<3){
            Vector<Long> ids = new Vector<Long>();
            ids.add(id);
            ClusteredLocation cl =getClusteredLocationWithIDs(ids).get(0);
            locs.add(new UserLocation(cl.getLoc(),cl.getDate(),cl.getId()));
        }
        return getConvextHull(locs);

    }
    public  List<UserLocation> getConvextHull(List<UserLocation> locs){

        if(locs.size()>2){
            Point2D[] points = new Point2D[locs.size()];
            int i=0;
            for(UserLocation loc:locs){
                points[i++]= new Point2D(loc.getLoc().lat,loc.getLoc().lon, loc.getDate());
            }
            GrahamScan gs = new GrahamScan(points);
            Iterable<Point2D> resHull = gs.hull();
            ArrayList<UserLocation> reslocs = new ArrayList<UserLocation>();
            for(Point2D resPoint:resHull){
                reslocs.add(new UserLocation(Location.coord((int)resPoint.x(),(int)resPoint.y()), resPoint.getId(), locs.get(0).getParentCluster()));
            }
            return reslocs;
        }
        return locs;

    }
    public boolean compareHulls(List<UserLocation> hull1,List<UserLocation> hull2){
        for(UserLocation loc:hull1){
            if(!hull2.contains(loc))return false;
        }
        return true;
    }
    public boolean areOverlapping(List<UserLocation> hull1,List<UserLocation> hull2){
        SpatialReference sr = SpatialReference.create(4326);
        Polygon poly1 = new Polygon();
        Polygon poly2 = new Polygon();
        for(UserLocation uloc: hull1){
          if(hull1.indexOf(uloc)==0){
              poly1.startPath(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
          }else{
              poly1.lineTo(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
          }
        }
        for(UserLocation uloc: hull2){
            if(hull2.indexOf(uloc)==0){
                poly2.startPath(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
            }else{
                poly2.lineTo(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
            }
        }
        boolean touches = GeometryEngine.touches(poly1,poly2,sr);
        boolean intersects = OperatorIntersects.local().execute(poly1,poly2,sr, null);
        boolean contains = GeometryEngine.contains(poly1,poly2,sr);
        OperatorIntersects.local().execute(poly1,poly2,sr, null);
        return contains || intersects || touches;
        
    }
    public boolean coversLocations(List<UserLocation> polygon, List<UserLocation> points){
        SpatialReference sr = SpatialReference.create(4326);
        Polygon poly1 = new Polygon();
        for(UserLocation uloc: polygon){
            if(polygon.indexOf(uloc)==0){
                poly1.startPath(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
            }else{
                poly1.lineTo(uloc.getLoc().getLatAsDouble(),uloc.getLoc().getLonAsDouble());
            }
        }
        MultiPoint pts2check = new MultiPoint();
        for(UserLocation pts:points){
            pts2check.add(pts.getLoc().getLatAsDouble(),pts.getLoc().getLonAsDouble());
        }
        boolean touches = GeometryEngine.touches(poly1,pts2check,sr);
        boolean intersects = OperatorIntersects.local().execute(poly1,pts2check,sr, null);
        boolean contains = GeometryEngine.contains(poly1,pts2check,sr);
        OperatorIntersects.local().execute(poly1,pts2check,sr, null);
        return contains || intersects || touches;
    }
    public boolean coversLocation(List<UserLocation> polygon, UserLocation point){
        Vector<UserLocation> points = new Vector<UserLocation>();
        points.add(point);
        return coversLocations(polygon, points);
    }


    private List<LatLng> createPolygon(List<UserLocation> boundingLocations){
        List<LatLng> polyNew = new ArrayList<LatLng>();
        for(UserLocation cCLoc: boundingLocations){
            polyNew.add(PTNMELocationManager.getManager().getLatLng(cCLoc.getLoc()));
        }
        return polyNew;
    }
    public  ClusteredLocation getClusteredLocation(long id){
       for(ClusteredLocation cl: getClusteredLocationsFromCache(true)){
           if(cl.getId() == id){
               return cl;
           }
       }
    return null;
    }
    private List<UserLocation> dataset2ULList(Dataset data){
        ArrayList<UserLocation> result = new ArrayList<UserLocation>();
        for(int i=0; i<data.size(); i++){
            result.add((UserLocation) data.get(i));
        }
        return result;
    }

}
