package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox;

import java.util.GregorianCalendar;
import java.util.List;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by Dennis on 01.10.2017.
 */

@BaseEntity
public abstract class SensorTimeseries {
    @Id
    public long id;
    @Index
    public long timestamp;
    @Index
    public String timestamp_day;
    public boolean uploaded;
    public boolean current;

    public List<SensorObject> values;

    public SensorTimeseries() {
    }

    public SensorTimeseries(long timestamp) {
        this.timestamp = timestamp;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        uploaded = false;
    }
}
