package de.dennis.mobilesensing_module.mobilesensing;

import org.greenrobot.greendao.database.Database;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.DaoMaster;
import de.dennis.mobilesensing_module.mobilesensing.EventBus.DaoSession;
import de.dennis.mobilesensing_module.mobilesensing.Storage.StorageManager;

/**
 * Created by Dennis on 10.07.2017.
 */

public class Module extends android.app.Application{
    private static DaoSession daoSession;
    //
    private static StorageManager storMang;
    @Override
    public void onCreate() {
        super.onCreate();
        storMang = new StorageManager();
        // DAO
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "sensor-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    public static StorageManager getStorageManager(){
        return storMang;
    }
    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
