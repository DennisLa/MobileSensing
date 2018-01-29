package de.ms.ptenabler.locationtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.ms.ptenabler.util.Utilities;

/**
 * Created by Martin on 08.06.2016.
 */
public class TimeTickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PTEnabler", "Received 24h tick ... Checking if necessary");
        LocationService service =PTNMELocationManager.getManager().getService();
        if(service!=null)service.checkIfClusteringDue();
    }
}
