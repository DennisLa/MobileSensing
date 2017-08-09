package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;

/**
 * Created by Dennis on 23.06.2017.
 */
public class SensorDataEvent {
    public final SensorTimeseries data;

    public SensorDataEvent(SensorTimeseries data) {
        this.data = data;
    }
}
