package de.ms.ptenabler.locationtools.trajectory;

import android.location.LocationManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import de.ms.ptenabler.locationtools.ClusteredLocation;
import de.ms.ptenabler.locationtools.PTNMELocationManager;
import de.ms.ptenabler.locationtools.UserLocation;
import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.dto.Location;

/**
 * Created by Martin on 30.10.2015.
 */
public class Trajectory {
    public long enter_time;
    public long exit_time;
    public TreeSet<TrajectoryElement> elements;
    public Trajectory(){
        this.enter_time=new Date().getTime();
        this.exit_time = 0l;
        elements = new TreeSet<TrajectoryElement>();
    }
    public void addElement(TrajectoryElement element){
        this.enter_time = Math.min(this.enter_time, element.getStart());
        this.exit_time = Math.max(this.exit_time, element.getEnd());
        elements.add(element);
    }
    public void addMultipleElements(Collection<TrajectoryElement> toAdd){
        for(TrajectoryElement element : toAdd){
            addElement(element);
        }
    }

    /**
     *
     * @param timeThresholdInMillis the duration a path at least needs to last in order to be valid
     * @return A cleaned Trajectory, which is created by copying all elements of the current Trajectory and filter paths based on the provided criteria and internal reasoning.
     */
    public Trajectory cleanTrajectory(long timeThresholdInMillis){
        Trajectory result = new Trajectory();
        Iterator<TrajectoryElement> it = this.elements.iterator();
        TrajectoryElement [] array = mergeElements(this.elements.toArray(new TrajectoryElement[this.elements.size()]), timeThresholdInMillis);
        for(TrajectoryElement arrayMent: array){
            result.addElement(arrayMent);
        }
        return result;
    }

    /**
     *
     * @param start Center of cluster or start of path to be used as benchmark
     * @param path List containing all Locations that describe the path
     * @param threshold min duration of the path
     * @return The return value is the quotient of the sum of absolute distances from each point of the path divided by the length of the path.
     * All values lower than 1 indicate that the path is circling around the start location, which suggests GPS errors
     */
    private boolean testForRealPath(UserLocation start, List<UserLocation> path, long threshold){
        return (path.size()>3&&
                path.get(path.size()-1).getDate()-path.get(0).getDate()>threshold);

    }
    private TrajectoryElement[] mergeElements(TrajectoryElement[] array, long threshold){
        Vector<TrajectoryElement> vector = new Vector<TrajectoryElement>();
        for(TrajectoryElement element :array){
            vector.add(element);
        }
        for(int i=0; i<vector.size()-2; i++){
            TrajectoryElement first = vector.get(i);
            TrajectoryElement second = vector.get(i+1);
            TrajectoryElement third = vector.get(i+2);
            if(first instanceof TrajectoryNode && third instanceof TrajectoryNode && second instanceof TrajectoryPath){
                if(((TrajectoryNode) first).nodeLocation.equals(((TrajectoryNode) third).nodeLocation)){
                    if(testForRealPath(new UserLocation(((TrajectoryNode) first).nodeLocation.getLoc(),
                            first.getStart(),
                            ((TrajectoryNode) first).nodeLocation.getId()),
                            ((TrajectoryPath) second).locations,
                            threshold)){
                        continue;
                    }else{
                        vector.remove(second);
                        for(UserLocation loc:((TrajectoryNode) third).locations){
                            ((TrajectoryNode) first).addLocationSample(loc);
                        }
                        vector.remove(third);
                        i=0;
                        continue;

                    }
                }
            }
            if(first instanceof TrajectoryNode && second instanceof TrajectoryNode){
                if(((TrajectoryNode) first).nodeLocation.equals(((TrajectoryNode) second).nodeLocation)){
                    for(UserLocation loc:((TrajectoryNode) second).locations){
                        ((TrajectoryNode) first).addLocationSample(loc);
                    }
                    vector.remove(second);
                    i=0;
                }
            }
            if(first instanceof TrajectoryPath && second instanceof TrajectoryPath){
                ((TrajectoryPath) first).addMultipleElements(((TrajectoryPath) second).locations);
                vector.remove(second);
                i=0;
            }
        }
        return vector.toArray(new TrajectoryElement[vector.size()]);
    }

    public JSONObject toGeoJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "FeatureCollection");
            JSONArray features = new JSONArray();
            for(TrajectoryElement feature:elements){
                features.put(feature.toGeoJSON());
            }
            obj.put("features", features);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
