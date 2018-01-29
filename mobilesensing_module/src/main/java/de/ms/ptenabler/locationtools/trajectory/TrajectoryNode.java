package de.ms.ptenabler.locationtools.trajectory;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import de.ms.ptenabler.locationtools.ClusterManagement;
import de.ms.ptenabler.locationtools.ClusteredLocation;
import de.ms.ptenabler.locationtools.PTNMELocationManager;
import de.ms.ptenabler.locationtools.UserLocation;
import de.ms.ptenabler.util.Utilities;

/**
 * Created by Martin on 30.10.2015.
 */
public class TrajectoryNode implements TrajectoryElement, Comparable<TrajectoryElement>{
    public ClusteredLocation nodeLocation;
    public long start_time;
    public long end_time;
    public TreeSet<UserLocation> locations;
    public TrajectoryNode(ClusteredLocation loc, long start, long end){
        this.nodeLocation = loc;
        this.start_time = start;
        this.end_time = end;
        this.locations = new TreeSet<UserLocation>();
    }
    public TrajectoryNode(ClusteredLocation loc){
        this.nodeLocation = loc;
        this.start_time = new Date().getTime();
        this.end_time = 0l;
        this.locations = new TreeSet<UserLocation>();
    }

    public void addLocationSample(UserLocation loc){
        this.start_time = Math.min(this.start_time, loc.getDate());
        this.end_time = Math.max(this.end_time, loc.getDate());
        locations.add(loc);
    }
    @Override
    public int compareTo(TrajectoryElement another) {
        long res = this.start_time-another.getStart();
        if(res==0) return 0;
        return (res<0) ? -1:1;
    }

    @Override
    public long getStart() {
        return start_time;
    }

    @Override
    public long getEnd() {
        return end_time;
    }

    @Override
    public long getDuration() {
        return getEnd()-getStart();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TrajectoryNode){
           return this.compareTo((TrajectoryElement)o) ==0;
        }
        return false;
    }

    @Override
    public String toString() {
        return ""+getStart()+"\t"+ new Gson().toJson(nodeLocation);
    }

    public String toReadableString(){
        return ""+new Date(getStart()).toLocaleString()+": " + nodeLocation.getLoc().place;
    }

    @Override
    public JSONObject toGeoJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "Feature");
            JSONArray collection = new JSONArray();
            JSONObject hull  = new JSONObject();
            hull.put("type", "Polygon");
            JSONArray points = new JSONArray();
            List<UserLocation> list = nodeLocation.getMeta().getHull();
            //Create valid linear Ring to comply with GeoJSON Specs
            do{
                list.add(list.get(0));
            }while(list.size()<4);

            Iterator<UserLocation> it = list.iterator();
            while(it.hasNext()){
                UserLocation uloc = it.next();
                JSONArray loc = new JSONArray();
                double[] latlng = uloc.getLatLng();
                loc.put(0, latlng[1]);
                loc.put(1, latlng[0]);
                loc.put(2,0);
                loc.put(3, uloc.getDate());
                loc.put(4, uloc.getParentCluster());
                points.put(loc);
            }
            JSONArray outRing = new JSONArray();
            outRing.put(points);
            hull.put("coordinates", outRing);
            collection.put(hull);

            JSONObject center  = new JSONObject();
            center.put("type", "Point");
            JSONArray pointsOfCenter = new JSONArray();
            pointsOfCenter.put(nodeLocation.getLoc().getLonAsDouble());
            pointsOfCenter.put(nodeLocation.getLoc().getLatAsDouble());
            center.put("coordinates", pointsOfCenter);
            collection.put(center);
            JSONObject collectioObj = new JSONObject();
            collectioObj.put("type","GeometryCollection");
            collectioObj.put("geometries", collection);
            JSONObject props = new JSONObject();
            props.put("start", start_time);
            props.put("end", end_time);
            props.put("cluster", nodeLocation.getId());
            props.put("count", nodeLocation.getCount());
            props.put("firstseen", nodeLocation.getFirstseen());
            props.put("lastClustered", nodeLocation.getDate());
            props.put("name", nodeLocation.getLoc().place);
            props.put("address", nodeLocation.getLoc().name);
            obj.put("properties", props);
            obj.put("geometry",collectioObj );
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
