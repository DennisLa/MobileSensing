package de.dennis.mobilesensing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dennis on 07.11.2017.
 */

public class BootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Application.startSensing();
    }

}
