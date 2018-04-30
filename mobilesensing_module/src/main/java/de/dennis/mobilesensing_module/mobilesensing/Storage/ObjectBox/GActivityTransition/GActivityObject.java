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

    public long endtime;
    public String activity;

    public ToOne<GActivityTimeseries> gActivityTimeseries;


    public GActivityObject(long timestamp, long endtime, String activity) {
        super(timestamp,GActivityTimeseries.class.getName());
        this.endtime = endtime;
        this.activity = activity;
    }
}
