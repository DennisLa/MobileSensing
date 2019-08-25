package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 31.03.2018.
 */
@Entity
public class GActivityObject extends SensorObject {
    @Id
    public long id;
    @Index
    public long timestamp;
    public long endtime;
    public String activity;

    public ToOne<GActivityTimeseries> gActivityTimeseries;

    public GActivityObject() {
        int i =0 ;
    }

    public GActivityObject(long timestamp, long endtime, String activity) {
        this.timestamp = timestamp;
        this.endtime = endtime;
        this.activity = activity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getEndtime() {
        return endtime;
    }

    public String getActivity() {
        return activity;
    }
}
