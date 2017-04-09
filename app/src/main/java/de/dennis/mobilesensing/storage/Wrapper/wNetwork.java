package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 22.03.2017.
 */
public class wNetwork {
    private String networkType;
    private long timestamp;

    public wNetwork(String networkType, long timestamp){
        this.networkType=networkType;
        this.timestamp=timestamp;
    }

    public String getNetworkType() {
        return networkType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
