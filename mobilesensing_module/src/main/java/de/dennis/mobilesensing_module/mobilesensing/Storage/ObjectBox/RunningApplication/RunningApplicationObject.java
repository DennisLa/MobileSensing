package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplication;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class RunningApplicationObject extends SensorObject {
    @Id
    public long id;
    public String applicationName;

    public ToOne<RunningApplicationTimeseries> runningApplicationTimeseries;

    public RunningApplicationObject(long timestamp, String applicationName) {
        super(timestamp,RunningApplicationTimeseries.class.getName());
        this.applicationName = applicationName;
    }

    public RunningApplicationObject() {
    }
}
