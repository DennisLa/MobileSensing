package de.ms.ptenabler.locationtools;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import de.schildbach.pte.dto.Location;


/**
 * Created by Martin on 19.05.2015.
 */
public class ClusterMetaData implements Serializable{
    public enum ClusterType{
        NOISE_CLUSTER,
        DAILY_CLUSTER
    }

    public boolean deleted;
    public boolean modified;
    public ClusterType type=ClusterType.DAILY_CLUSTER;
    public Vector<Long> nextCIDs;
    public Vector<Double> nextCIDprobs;
    public Vector<long[]> hull;
    public String poiCategory;
    public transient Vector<UserLocation>realHull=null;
    public static Gson gson;
    public ClusterMetaData(String json){
       if(gson==null) gson = new Gson();
            try{
                ClusterMetaData temp = gson.fromJson(json, ClusterMetaData.class);
                if(temp!=null){
                    this.deleted = temp.deleted;
                    this.modified = temp.modified;
                    this.nextCIDs = temp.nextCIDs;
                    this.nextCIDprobs = temp.nextCIDprobs;
                    this.poiCategory = temp.poiCategory;
                    this.type = temp.type;
                    this.hull = temp.hull;
                }else{
                    initDefaults(type);
                }
            }catch(Exception e) {
               initDefaults(type);
            }
        }





    public ClusterMetaData(ClusterType type){
        if(gson==null) gson = new Gson();
        initDefaults(type);
        }

    private void initDefaults(ClusterType type){
        this.deleted = false;
        this.modified = false;
        this.nextCIDs = new Vector<Long>();
        this.nextCIDprobs = new Vector<Double>();
        this.type = type;
        this.hull = new Vector<long[]>();
    }

    public List<UserLocation> getHull(){
        if(realHull==null){
            realHull= new Vector<UserLocation>();
            for(long[] current:hull){
                realHull.add(new UserLocation(Location.coord((int)current[0],(int)current[1]),current[2],current[3]));
            }
        }

        return realHull;
    }
    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
