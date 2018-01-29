package de.ms.ptenabler.locationtools.trajectory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import de.ms.ptenabler.locationtools.PTNMELocationManager;
import de.ms.ptenabler.locationtools.UserLocation;

/**
 * Created by Martin on 30.10.2015.
 */
public class TrajectoryPath implements TrajectoryElement, Comparable<TrajectoryElement>{
    public long start_time;
    public long end_time;
    public Vector<UserLocation> locations;

    public TrajectoryPath(){
        start_time = new Date().getTime();
        end_time = 0l;
        locations = new Vector<UserLocation>();
    }

    @Override
    public int compareTo(TrajectoryElement another) {
        long res = this.start_time-another.getStart();
        if(res==0) return 0;
        return (res<0) ? -1:1;
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof TrajectoryElement){
            return this.getStart() == ((TrajectoryElement)o).getStart();
        }
        return false;
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

    public void addElement(UserLocation element){
        this.start_time = Math.min(this.start_time, element.getDate());
        this.end_time = Math.max(this.end_time, element.getDate());
        locations.add(element);
    }
    public void addMultipleElements(Collection<UserLocation> toAdd){
        for(UserLocation element : toAdd){
            addElement(element);
        }
    }
    public String toReadableString(){
        return ""+new Date(getStart()).toLocaleString()+": " +(end_time-start_time)/(1000l*60l) + "Minuten";
    }

    @Override
    public JSONObject toGeoJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "Feature");
            JSONObject geometry  = new JSONObject();
            geometry.put("type", "LineString");
            double distance =0;
            JSONArray points = new JSONArray();
            while(this.locations.size()<4){

                    this.locations.add(this.locations.get(this.locations.size()-1));

            }
            Iterator<UserLocation> it = this.locations.iterator();
            UserLocation last =null;
            while(it.hasNext()){

                UserLocation uloc = it.next();
                if(last==null ) last = uloc; // first location

                JSONArray loc = new JSONArray();
                double[] latlng = uloc.getLatLng();
                loc.put(0, latlng[1]);
                loc.put(1, latlng[0]);
                loc.put(2,0);
                loc.put(3, uloc.getDate());
                loc.put(4, uloc.getParentCluster());
                points.put(loc);
                distance += PTNMELocationManager.getManager().computeDistance(last.getLoc(), uloc.getLoc());
                last = uloc;
            }
            //Create valid linear Ring to comply with GeoJSON Specs
            long duration = (long)(((JSONArray)points.get(points.length()-1)).get(3))-(long)(((JSONArray)points.get(0)).get(3));
            double inMinutes = (double)duration/60000.0;
            geometry.put("coordinates", points);
            obj.put("geometry",geometry );
            JSONObject props = new JSONObject();
            props.put("start", start_time);
            props.put("end", end_time);
            props.put("distance", distance);
            props.put("duration", inMinutes);
            obj.put("properties", props);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
