package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ScreenOn;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class ScreenOnObject extends SensorObject {
    @Id
    public long id;
    @Index
    public long timestamp;
    public boolean screenOn;

    public ToOne<ScreenOnTimeseries> screenOnTimeseries;

    public ScreenOnObject(long timestamp, boolean screenOn) {
        this.timestamp = timestamp;
        this.screenOn = screenOn;
    }

    public ScreenOnObject(){

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isScreenOn() {
        return screenOn;
    }

    public void setScreenOn(boolean screenOn) {
        this.screenOn = screenOn;
    }

    public void setScreenOnTimeseries(ToOne<ScreenOnTimeseries> screenOnTimeseries) {
        this.screenOnTimeseries = screenOnTimeseries;
    }

    public ToOne<ScreenOnTimeseries> getScreenOnTimeseries() {
        return screenOnTimeseries;
    }

    public boolean getScreenOn() {
        return screenOn;
    }
}
