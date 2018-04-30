package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class ActivityObject extends SensorObject {
    @Id
    public long id;
    public String activity;
    public int probability;

    public ToOne<ActivityTimeseries> sensorTimeseries;

    public ActivityObject(long timestamp, String activity, int probability) {
        super(timestamp, ActivityTimeseries.class.getName());
        this.activity = activity;
        this.probability = probability;
    }

    public ActivityObject() {
    }
}
