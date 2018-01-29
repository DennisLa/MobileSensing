package de.ms.ptenabler.Message;

import de.ms.ptenabler.locationtools.ClusteredLocation;
import de.ms.ptenabler.locationtools.UserLocation;

/**
 * Created by Martin on 19.03.2016.
 */
public class ClusterMessage {

  public enum STATE{
        LEAVING,
        ENTERING,
        INSIDE
    }

    public ClusteredLocation cloc;
    public STATE state;
    public UserLocation cause;

    public ClusterMessage(ClusteredLocation cloc, STATE state, UserLocation cause){
        this.cloc = cloc;
        this.state =state;
        this.cause = cause;
    }

}
