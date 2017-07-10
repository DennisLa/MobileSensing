package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import org.json.JSONObject;

import de.dennis.mobilesensing_module.mobilesensing.Sensors.SensorNames;

/**
 * Created by Dennis on 23.06.2017.
 */
public class NewSensorDataEvent {
    public final SensorObject data;
    public final SensorNames sensorName;

    public NewSensorDataEvent(SensorObject data, SensorNames sensorName) {
        this.data = data;
        this.sensorName = sensorName;
    }
}
