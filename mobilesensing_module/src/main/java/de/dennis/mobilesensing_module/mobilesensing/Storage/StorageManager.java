package de.dennis.mobilesensing_module.mobilesensing.Storage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.DaoSession;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.NewSensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorObjectDao;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Sensors.SensorNames;

import static de.dennis.mobilesensing_module.mobilesensing.Sensors.SensorNames.LOCATION;

/**
 * Created by Dennis on 10.07.2017.
 */

public class StorageManager {

    public StorageManager()
    {
        EventBus.getDefault().register(this);
    }
    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(NewSensorDataEvent event) {
        DaoSession daoSession = Module.getDaoSession();
        SensorObjectDao sd = daoSession.getSensorObjectDao();
        switch (event.sensorName) {
            case LOCATION:
                break;
            default:
                break;
        }
    }

}
