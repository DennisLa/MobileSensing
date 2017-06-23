package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.ScreenOnService;

/**
 * Created by Dennis on 04.05.2017.
 */
public class ScreenOn {
    private long timestamp;
    private boolean isScreenOn;

    public ScreenOn(long timestamp, boolean isScreenOn){
        this.timestamp = timestamp;
        this.isScreenOn = isScreenOn;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isScreenOn() {
        return isScreenOn;
    }
}
