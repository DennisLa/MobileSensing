package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track;

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
public class TrackObject extends SensorObject {
    @Id
    public long id;
    @Index
    public long startTimestamp;
    public long endTimestamp;

    public boolean edited;

    //TODO Rating einfügen

    public ToOne<TrackTimeseries> trackTimeseries;

    public TrackObject(long startTimestamp, long endTimestamp) {
        super(startTimestamp,TrackTimeseries.class.getName());
        this.endTimestamp = endTimestamp;
        this.edited = false;
    }
    public TrackObject() {
    }
}
