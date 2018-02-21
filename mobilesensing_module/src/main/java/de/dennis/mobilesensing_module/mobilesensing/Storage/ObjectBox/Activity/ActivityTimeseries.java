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
    @Index
    public long timestamp;
    @Index
    public String timestamp_day;
    public boolean uploaded;
    public boolean current;
    @Backlink
    public ToMany<ActivityObject> values;


    public ActivityTimeseries(long timestamp) {
        this.timestamp = timestamp;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+g.get(GregorianCalendar.MONTH)+1+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        uploaded = false;
    }

    public ActivityTimeseries(){

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp_day() {
        return timestamp_day;
    }

    public void setTimestamp_day(String timestamp_day) {
        this.timestamp_day = timestamp_day;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public ToMany<ActivityObject> getValues() {
        return values;
    }

    public void setValues(ToMany<ActivityObject> values) {
        this.values = values;
    }

    public boolean getUploaded() {
        return uploaded;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
