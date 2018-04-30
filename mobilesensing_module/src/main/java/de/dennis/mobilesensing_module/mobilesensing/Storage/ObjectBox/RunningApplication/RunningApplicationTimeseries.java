package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication;

import java.util.GregorianCalendar;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class RunningApplicationTimeseries extends SensorTimeseries {
    @Id
    public long id;

    @Backlink
    public ToMany<RunningApplicationObject> values;

    public RunningApplicationTimeseries(long timestamp) {
        super(timestamp);
    }

    public RunningApplicationTimeseries() {
    }
}

