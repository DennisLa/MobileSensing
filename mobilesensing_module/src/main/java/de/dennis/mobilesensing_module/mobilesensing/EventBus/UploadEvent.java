package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;

/**
 * Created by Dennis on 17.09.2017.
 */

public class UploadEvent {
    public final SensorTimeseries data;
    public UploadEvent(SensorTimeseries data) {
        this.data = data;
    }
}
