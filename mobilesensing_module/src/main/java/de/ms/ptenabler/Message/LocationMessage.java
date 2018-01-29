package de.ms.ptenabler.Message;

import de.schildbach.pte.dto.Location;

/**
 * Created by Martin on 29.02.2016.
 */
public class LocationMessage
{
    public long timestamp;
    public Location loc;
    public LocationMessage(Location loc, long timestamp){
        if(loc==null)throw new IllegalArgumentException("Location must not be null");
        this.loc=loc;
        this.timestamp=timestamp;
    }
}
