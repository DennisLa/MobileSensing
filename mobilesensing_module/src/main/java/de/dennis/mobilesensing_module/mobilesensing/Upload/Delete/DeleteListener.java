package de.dennis.mobilesensing_module.mobilesensing.Upload.Delete;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;

/**
 * Created by Dennis on 29.10.2017.
 */

public class DeleteListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        //now - 7 days
        c.setTimeInMillis(c.getTimeInMillis() - 604800000);
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
        oba.deleteAllSensorTimeseriesOlder(c.getTimeInMillis());
    }
}
