package de.ms.ptenabler.locationtools.trajectory;

import java.util.Vector;

import de.ms.ptenabler.locationtools.ClusteredLocation;
import de.ms.ptenabler.locationtools.PropableNextLocationResult;

/**
 * Created by Martin on 13.06.2016.
 */
public class PNLMessage {

    public long timestamp;
    public Vector<PropableNextLocationResult> locations;
    public ClusteredLocation currentLocation;

    public PNLMessage(long ts, Vector<PropableNextLocationResult> locations, ClusteredLocation currentLocation){
        this.timestamp=ts;
        this.locations = locations;
        this.currentLocation = currentLocation;
    }

}
