package de.dennis.mobilesensing.storage;

import de.dennis.mobilesensing.Application;

/**
 * Created by Dennis on 21.03.2017.
 */
public class StorageHelper {
    private static DataAdapter adap;
    public static DataAdapter openDBConnection(){
        if(adap== null ){
            adap = new DataAdapter(Application.getContext());
        }
        adap.open();
        return adap;
    }
}
