package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Cluster;

import com.google.gson.Gson;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import de.ms.ptenabler.locationtools.ClusterMetaData;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 14.11.2017.
 */
@Entity
public class ClusterObject extends SensorObject {

    @Id
    public long id;
    public long date;
    public long firstseen;
    public int count;
    public double lat;
    public double lng;
    public String metajson;

    /*
    private static final long serialVersionUID = 1L;
	private Location loc;
	private long date;
	private long firstseen;
	private int count;
	private long id;
    private ClusterMetaData meta;
     */

    public ClusterObject(long date, long firstseen, int count, double lat, double lng, String metajson){
        this.date = date;
        this.firstseen = firstseen;
        this.count = count;
        this.lat = lat;
        this.lng = lng;
        this.metajson = metajson;
    }
    public ClusterObject(long id, long date, long firstseen, int count, double lat, double lng, String metajson){
        this.id = id;
        this.date = date;
        this.firstseen = firstseen;
        this.count = count;
        this.lat = lat;
        this.lng = lng;
        this.metajson = metajson;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFirstseen() {
        return firstseen;
    }

    public void setFirstseen(long firstseen) {
        this.firstseen = firstseen;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getTimestamp() {
        return firstseen;
    }
}
