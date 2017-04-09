package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 22.03.2017.
 */
public class wRunningApplication {
    private String packagename;
    private long timestamp;

    public wRunningApplication(String packagename, long timestamp){
        this.packagename=packagename;
        this.timestamp=timestamp;
    }

    public String getPackagename() {
        return packagename;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
