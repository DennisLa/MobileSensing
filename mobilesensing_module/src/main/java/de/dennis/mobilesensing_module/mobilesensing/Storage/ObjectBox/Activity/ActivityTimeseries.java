package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Activity;

import java.util.GregorianCalendar;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;


/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class ActivityTimeseries extends SensorTimeseries {
    @Id
    public long id;
    @Backlink
    public ToMany<ActivityObject> values;


    public ActivityTimeseries(long timestamp) {
        super(timestamp);
    }

    public ActivityTimeseries(){

    }
}
