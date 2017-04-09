package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 21.03.2017.
 */
public class wLocation {
    private long timestamp;
    private double latitude;
    private double longitude;
    private double velocity;
    private double altitude;
    private double accuracy;

    public wLocation(long timestamp, double latitude, double longitude,double velocity, double altitude, double accuracy){
        this.timestamp =timestamp;
        this.latitude=latitude;
        this.longitude=longitude;
        this.velocity=velocity;
        this.accuracy=accuracy;
        this.altitude=altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getAltitude() {
        return altitude;
    }
}
