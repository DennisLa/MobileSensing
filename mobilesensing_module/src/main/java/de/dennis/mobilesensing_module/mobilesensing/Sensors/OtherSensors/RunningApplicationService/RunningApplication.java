package de.dennis.mobilesensing_module.mobilesensing.Sensors.OtherSensors.RunningApplicationService;

/**
 * Created by Dennis on 21.03.2017.
 */
public class RunningApplication {
    String mPackagename;
    Long mTimestamp;

    public RunningApplication(String packagename, Long timestamp){
        mPackagename=packagename;
        mTimestamp=timestamp;
    }

    public String getPackagename() {
        return mPackagename;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }
}
