package de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Network;

import java.util.GregorianCalendar;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.BoxStore;

/**
 * Created by Dennis on 01.10.2017.
 */
@Entity
public class NetworkTimeseries extends SensorTimeseries {
    @Id
    public long id;

    @Backlink
    public ToMany<NetworkObject> values;

    public NetworkTimeseries(long timestamp) {
        this.timestamp = timestamp;
        GregorianCalendar g = new GregorianCalendar();
        g.setTimeInMillis(timestamp);
        timestamp_day = g.get(GregorianCalendar.YEAR)+"-"+(g.get(GregorianCalendar.MONTH)+1)+"-"+g.get(GregorianCalendar.DAY_OF_MONTH)+"T"+"00:00:00.000Z";
        uploaded = false;
    }
    public NetworkTimeseries() {
    }
}
