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
    @Index
    public long timestamp;
    @Index
    public String timestamp_day;
    public boolean uploaded;
    public boolean current;
    @Backlink
    public ToMany<GActivityObject> values;
    public GActivityTimeseries(long timestamp) {
        this.timestamp = timestamp;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
//        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+ g.get(GregorianCalendar.HOUR_OF_DAY) + ".000Z";
        uploaded = false;
    }
    public GActivityTimeseries(){

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public ToMany<GActivityObject> getValues() {
        return values;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public String getTimestamp_day() {
        return timestamp_day;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}
