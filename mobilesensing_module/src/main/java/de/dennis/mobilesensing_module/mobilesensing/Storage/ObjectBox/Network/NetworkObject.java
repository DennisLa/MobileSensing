package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class NetworkObject extends SensorObject {
    @Id
    public long id;
    public String networkType;

    public ToOne<NetworkTimeseries> networkTimeseries;

    public NetworkObject(long timestamp, String networkType) {
        super(timestamp,NetworkTimeseries.class.getName());
        this.networkType = networkType;
    }

    public NetworkObject() {
    }
}
