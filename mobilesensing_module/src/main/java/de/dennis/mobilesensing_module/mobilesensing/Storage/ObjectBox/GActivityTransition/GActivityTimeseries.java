package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GActivityTransition;

import java.util.GregorianCalendar;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;

/**
 * Created by Dennis on 31.03.2018.
 */
@Entity
public class GActivityTimeseries extends SensorTimeseries {
    @Id
    public long id;

    @Backlink
    public ToMany<GActivityObject> values;

    public GActivityTimeseries(long timestamp) {
        super(timestamp);
    }
    public GActivityTimeseries(){

    }
}
