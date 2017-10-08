package de.dennis.mobilesensing_module.mobilesensing.Storage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;

/**
 * Created by Dennis on 05.08.2017.
 */

public class StorageEventListener {

    public StorageEventListener() {
        EventBus.getDefault().register(this);
    }

    // This method will be called when a new SensorDataEvent arrived
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(SensorDataEvent event) {
        ObjectBoxAdapter obd = new ObjectBoxAdapter();
        obd.saveSensorObject(event.data);
    }


}


