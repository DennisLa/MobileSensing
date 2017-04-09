package de.dennis.mobilesensing.storage.Wrapper;

/**
 * Created by Dennis on 21.03.2017.
 */
public class wActivity {
    private String mode;
    private long timestamp;
    private int probability;

    public wActivity(long timestamp, String mode, int probability)
    {
        this.timestamp = timestamp;
        this.mode = mode;
        this.probability = probability;

    }

    public String getMode() {
        return mode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getProbability() {
        return probability;
    }
}
