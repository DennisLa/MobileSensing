package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox;

import io.objectbox.annotation.BaseEntity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by Dennis on 01.10.2017.
 */

@BaseEntity
public abstract class SensorObject {
    @Id
    public long id;
    @Index
    public long timestamp;

    public String timeseriesClassName;

    public SensorObject(long timestamp, String timeseriesClassName){
         this.timestamp = timestamp;
         this.timeseriesClassName = timeseriesClassName;
    }
    public SensorObject(){

    }
}
