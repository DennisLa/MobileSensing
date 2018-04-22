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
    public boolean screenOn;

    public ToOne<ScreenOnTimeseries> screenOnTimeseries;

    public ScreenOnObject(long timestamp, boolean screenOn) {
       super(timestamp,ScreenOnTimeseries.class.getName());
        this.screenOn = screenOn;
    }

    public ScreenOnObject(){

    }
}
