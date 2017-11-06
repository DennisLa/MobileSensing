package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.RunningApplicationListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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
    @Index
    public long timestamp;
    @Index
    public String timestamp_day;
    public boolean uploaded;
    @Backlink
    public ToMany<RunningApplicationObject> values;
    /** Used to resolve relations */
    @Internal
    @Generated(1307364262)
    transient BoxStore __boxStore;

    public RunningApplicationTimeseries(long timestamp) {
        this.timestamp = timestamp;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+g.get(GregorianCalendar.MONTH)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        uploaded =false;
    }

    @Generated(116521068)
    @Internal
    /** This constructor was generated by ObjectBox and may change any time. */
    public RunningApplicationTimeseries(long timestamp, String timestamp_day, boolean uploaded) {
        this.timestamp = timestamp;
        this.timestamp_day = timestamp_day;
        this.uploaded = uploaded;
    }

    @Generated(1401392300)
    public RunningApplicationTimeseries() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getTimestamp_day() {
        return timestamp_day;
    }

    public void setTimestamp_day(String timestamp_day) {
        this.timestamp_day = timestamp_day;
    }

    public ToMany<RunningApplicationObject> getValues() {
        return values;
    }

    public void setValues(ToMany<RunningApplicationObject> values) {
        this.values = values;
    }

    public boolean getUploaded() {
        return uploaded;
    }
}

