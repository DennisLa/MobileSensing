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
    @Index
    public long timestamp;
    public String activity;
    public int probability;

    public ToOne<ActivityTimeseries> activityTimeseries;

    public ActivityObject(long timestamp, String activity, int probability) {
        this.timestamp = timestamp;
        this.activity = activity;
        this.probability = probability;
    }

    public ActivityObject() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        activity = activity;
    }

    public ToOne<ActivityTimeseries> getActivityTimeseries() {
        return activityTimeseries;
    }

    public void setActivityTimeseries(ToOne<ActivityTimeseries> activityTimeseries) {
        this.activityTimeseries = activityTimeseries;
    }
}
