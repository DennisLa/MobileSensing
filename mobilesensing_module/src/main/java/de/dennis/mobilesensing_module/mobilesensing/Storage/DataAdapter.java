package de.dennis.mobilesensing_module.mobilesensing.Storage;

import android.util.Log;

import java.util.List;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 05.08.2017.
 */

public class DataAdapter {
    public DataAdapter() {

    }

    public void saveTimeseriesToOB(SensorTimeseries st)
    {
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);

        Log.d("Test",st.getTimestamp_day());
        Log.d("Test",st.getSensor_info().getSensor_name());
        Log.d("Test", st.getSensor_infoToOne().getTarget().getSensor_name());
        long id = searchForTimeseriesOfSameDay(st.getTimestamp_day(),st.getSensor_infoToOne().getTarget().getSensor_name());
        if(id != 0){
            //Update Entity
            SensorTimeseries st_day = (SensorTimeseries) tsBox.get(id);
            List<SensorValue> lsv = st.getValues();
            Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
            //New SensorValue


        }else{
            //New Entity
            Box seBox = Module.getBoxStore().boxFor(StringEntitiy.class);
            Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
            Box siBox = Module.getBoxStore().boxFor(SensorInfo.class);
            Box viBox = Module.getBoxStore().boxFor(ValueInfo.class);

            seBox.put(st.getValues().get(0).getValues().get(0));
            svBox.put(st.getValues().get(0));
            siBox.put(st.getSensor_info());
            viBox.put(st.getSensor_info().getValue_info());
            tsBox.put(st);
            Log.d("DataAdapter","SensorTimeseriesSaved");
        }


    }

    public long searchForTimeseriesOfSameDay(String timestamp_day, String sensor_name ){
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        List<SensorTimeseries> lst = tsBox.getAll();
        long id = 0;
        for(SensorTimeseries st: lst){
            if(st.getTimestamp_day().equals(timestamp_day) && st.getSensor_info().getSensor_name().equals(sensor_name)){
                id = st.getTimestamp();
                break;
            }
        }
        return id;
    }

    public List<SensorTimeseries> getAllSensorTimeseries(){
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        return tsBox.getAll();
    }
}
