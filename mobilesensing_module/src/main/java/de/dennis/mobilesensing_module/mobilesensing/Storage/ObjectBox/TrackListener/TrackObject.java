package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.TrackListener;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.relation.ToOne;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class TrackObject extends SensorObject {
    @Id(assignable = true)
    protected long startTimestamp;
    protected long endTimestamp;

    protected ToOne<TrackTimeseries> trackTimeseries = new ToOne<>(this, TrackObject_.trackTimeseries);
    /** Used to resolve relations */
    @Internal
    @Generated(1307364262)
    transient BoxStore __boxStore;

    public TrackObject(long startTimestamp, long endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Generated(1786429799)
    @Internal
    /** This constructor was generated by ObjectBox and may change any time. */
    public TrackObject(long startTimestamp, long endTimestamp, long trackTimeseriesId) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.trackTimeseries.setTargetId(trackTimeseriesId);
    }

    @Generated(1279115072)
    public TrackObject() {
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setTrackTimeseries(ToOne<TrackTimeseries> trackTimeseries) {
        this.trackTimeseries = trackTimeseries;
    }

    public ToOne<TrackTimeseries> getTrackTimeseries() {
        return trackTimeseries;
    }

    public long getTimestamp() {
        return startTimestamp;
    }
}
