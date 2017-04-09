package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 22.03.2017.
 */
public class wDevicePosition {
    private String devicePositionType;
    private long timestamp;

    public wDevicePosition(String devicePositionType, long timestamp){
        this.devicePositionType = devicePositionType;
        this.timestamp =timestamp;
    }

    public String getDevicePositionType() {
        return devicePositionType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
