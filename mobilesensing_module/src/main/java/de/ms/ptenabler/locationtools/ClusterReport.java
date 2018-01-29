package de.ms.ptenabler.locationtools;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.ms.ptenabler.util.Utilities;

/**
 * Created by Martin on 12.07.2015.
 */
public class ClusterReport{
    public Date begins;
    public Date ends;
    public List<ClusteredLocation> clusters;
    public List<UserLocation> rawLocations;
    public int clusterCount;
    public int rawLocationCount;

    private ClusterReport(boolean includeRawLocations, long start){
        clusters= ClusterManagement.getManager().getClusteredLocationsFromCache(true);
        //rawLocations = Utilities.openDBConnection().getAllHistoryLocs(start, new Date().getTime());
        rawLocations = ObjectboxPtenablerUtilities.getAllHistoryLocs(start,new Date().getTime(), false);
        if(rawLocations.size()>0){
            begins = new Date(rawLocations.get(rawLocations.size()-1).getDate());
            ends = new Date(rawLocations.get(0).getDate());
        }else{
            begins= ends= new Date();
        }

        clusterCount = clusters.size();
        rawLocationCount = rawLocations.size();
        if(!includeRawLocations)rawLocations.clear();
    }
    public static ClusterReport generateReport(long start){
        return new ClusterReport(false, start);
    }

    public static ClusterReport generateReport(boolean include, long start){
        return new ClusterReport(include, start);
    }
}

