package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToOne;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class GLocationsObject extends SensorObject {
    @Id
    public long id;
    @Index
    public double lat;
    public double lng;
    public float speed;
    public boolean isClustered;
    @Index
    public long parentCluster;

    public ToOne<GLocationTimeseries> gLocationTimeseries;

    public GLocationsObject(long id, long timestamp, double lat, double lng, float speed, long parentCluster) {
        super(timestamp,GLocationTimeseries.class.getName());
        this.id = id;
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
        this.isClustered = false;
        this.speed = speed;
        this.parentCluster = parentCluster;
    }

    public GLocationsObject(long timestamp, double lat, double lng, float speed) {
        super(timestamp,GLocationTimeseries.class.getName());
        this.lat = lat;
        this.lng = lng;
        this.isClustered = false;
        this.speed = speed;
        this.parentCluster = 0L;
    }
    public GLocationsObject() {
    }
}
