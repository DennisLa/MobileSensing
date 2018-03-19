package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import java.util.ArrayList;

/**
 * Created by Dennis on 19.03.2018.
 */

public class MissingPermissionsEvent {
    public final ArrayList<String> permissionList;

    public MissingPermissionsEvent(ArrayList<String> permissionList) {
        this.permissionList=permissionList;
    }
}
