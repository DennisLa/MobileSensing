package de.dennis.mobilesensing.UI;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.MissingPermissionsEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;

/**
 * Created by Dennis on 19.03.2018.
 */

public class MissingPermissionListener {
    public MissingPermissionListener() {
        EventBus.getDefault().register(this);
    }

    // This method will be called when a new SensorDataEvent arrived
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MissingPermissionsEvent event) {
        Intent intent = new Intent(Module.getContext(), PermissionActivity.class);
        intent.putStringArrayListExtra("PermissionList",event.permissionList);
        Context context = Module.getContext();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Module.getContext().startActivity(intent);
    }
}
