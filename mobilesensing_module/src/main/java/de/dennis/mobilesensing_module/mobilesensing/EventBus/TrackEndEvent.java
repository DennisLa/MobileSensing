package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;

/**
 * Created by Dennis on 03.09.2017.
 */

public class TrackEndEvent {
    public final SensorTimeseries data;

    public TrackEndEvent(SensorTimeseries data) {
        this.data = data;
    }
}
