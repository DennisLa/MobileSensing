package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 22.03.2017.
 */
public class wCall {
    private String notificationType;
    private long timestamp;

    public wCall(String notificationType, long timestamp){
        this.notificationType = notificationType;
        this.timestamp = timestamp;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
