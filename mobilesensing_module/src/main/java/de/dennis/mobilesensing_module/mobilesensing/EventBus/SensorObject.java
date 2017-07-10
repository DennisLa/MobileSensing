package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import de.dennis.mobilesensing_module.mobilesensing.Sensors.SensorNames;

/**
 * Created by Dennis on 08.07.2017.
 * Parent Class of all Sensor Classes
 */
@Entity
class SensorObject {
    @Id
    private Long id;

    @NotNull
    private SensorNames sensorName;

    public SensorObject() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
