package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;

/**
 * Created by Dennis on 23.06.2017.
 */
public class SensorDataEvent {
    public final SensorObject data;

    public SensorDataEvent(SensorObject data) {
        this.data = data;
    }
}
