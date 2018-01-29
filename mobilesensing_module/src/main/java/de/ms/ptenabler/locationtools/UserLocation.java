package de.ms.ptenabler.locationtools;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.sf.javaml.core.DenseInstance;

import java.lang.reflect.Type;
import java.util.HashMap;

import de.schildbach.pte.dto.Location;

public class UserLocation extends DenseInstance implements Comparable<UserLocation>{

    private Location loc;
    private long date;
    private static final long serialVersionUID = -7202442547223420283L;
	private long parentCluster;
	
	public UserLocation(Location loc, long date, long parentCluster){
        super(new double[]{(double)loc.lat/1000000.0,(double)loc.lon/1000000.0,date,parentCluster});
        this.loc = loc;
        this.date = date;
        this.parentCluster = parentCluster;
	}

	public long getParentCluster() {
		return parentCluster;
	}

	public void setParentCluster(long parentCluster) {
		this.parentCluster = parentCluster;
	}
	public int compareTo(UserLocation cl2) {
		
		return new Long(getDate()).compareTo(new Long(cl2.getDate()));
	
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof UserLocation){
			return ((UserLocation)obj).compareTo(this)==0;
		}
		return false;
	}

    public Location getLoc() {
        return loc;
    }

    public double[] getLatLng(){
        return new double[]{(double)loc.lat/1000000.0,(double)loc.lon/1000000.0};
    }
    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }





}
