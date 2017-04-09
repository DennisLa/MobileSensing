package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 21.03.2017.
 */
public class wTrack {
    private long startTime;
    private long endTime;
    private double kilometers;
    private String mode;

    public wTrack(long startTime, long endTime, String mode, double kilometers)
    {
        this.startTime = startTime;
        this.endTime= endTime;
        this.kilometers = kilometers;
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public double getKilometers() {
        return kilometers;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }
}
