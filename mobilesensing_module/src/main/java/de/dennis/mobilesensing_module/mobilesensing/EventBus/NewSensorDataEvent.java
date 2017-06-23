package de.dennis.mobilesensing_module.mobilesensing.EventBus;

import org.json.JSONObject;

/**
 * Created by Dennis on 23.06.2017.
 */
public class NewSensorDataEvent {
    public final JSONObject data;

    public NewSensorDataEvent(JSONObject data) {
        this.data = data;
    }
}
