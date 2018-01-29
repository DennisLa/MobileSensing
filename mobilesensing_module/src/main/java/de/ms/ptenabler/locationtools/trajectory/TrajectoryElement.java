package de.ms.ptenabler.locationtools.trajectory;

import org.json.JSONObject;

/**
 * Created by Martin on 30.10.2015.
 */
public interface TrajectoryElement{
    public long getStart();
    public long getEnd();
    public long getDuration();
    public String toReadableString();
    public JSONObject toGeoJSON();


}
