package de.dennis.mobilesensing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.EditText;
import android.widget.Toast;

import de.dennis.mobilesensing.UI.ChatbotActivity;

/**
 * Created by Dennis on 02.11.2018.
 */

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*boolean isCharging = false;
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if(status == 2)
            isCharging = true;*/
        //boolean isCharging = status == BatteryManager.BATTERY_PLUGGED_AC ||
        //  status == BatteryManager.BATTERY_PLUGGED_USB;
        //BATTERY_STATUS_CHARGING;
        //  || status == BatteryManager.BATTERY_STATUS_FULL;
        /*
         * int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,
         * -1); boolean usbCharge = chargePlug ==
         * BatteryManager.BATTERY_PLUGGED_USB; boolean acCharge = chargePlug ==
         * BatteryManager.BATTERY_PLUGGED_AC;
         */
        Intent i = new Intent(context, ChatbotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        Toast.makeText(context, "Charging Change", Toast.LENGTH_SHORT).show();
    }
}
