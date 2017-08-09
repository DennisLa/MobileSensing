package de.dennis.mobilesensing_module.mobilesensing.Storage;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.MyObjectBox;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;
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
        //Log.d("Test", st.getSensor_infoToOne().getTarget().getSensor_name());
        long id = searchForTimeseriesOfSameDay(st.getTimestamp_day(),st.getSensor_info().getSensor_name());
        if(id != 0){
            //Update Entity
            SensorTimeseries st_day = (SensorTimeseries) tsBox.get(id);
            Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
            Box seBox = Module.getBoxStore().boxFor(StringEntity.class);
            //Putting SensorValue & StringEntity
            List<SensorValue> lsv = st.getValues();
            for(SensorValue sv: lsv){
                List<StringEntity> lse = sv.getValues();
                for(StringEntity se: lse){
                    se.setId(seBox.put(se));
                }
                sv.setValues(lse);
                sv.setId(svBox.put(sv));
            }
            for(SensorValue oldsv: st_day.getValues()){
                lsv.add(oldsv);
            }
            st_day.setValues(lsv);
            tsBox.put(st_day);
        }else{
            try{
            //New Entity
            Box seBox = Module.getBoxStore().boxFor(StringEntity.class);
            Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
            Box siBox = Module.getBoxStore().boxFor(SensorInfo.class);
            Box viBox = Module.getBoxStore().boxFor(ValueInfo.class);

            //Putting SensorValue & StringEntitiy
           List<SensorValue> lsv = st.getValues();
            for(SensorValue sv: lsv){
                List<StringEntity> lse = sv.getValues();
                for(StringEntity se: lse){
                   se.setId(seBox.put(se));

                }
                sv.setValues(lse);
                sv.setId(svBox.put(sv));
            }
            st.setValues(lsv);
            //Putting SensorInfo & ValueInfo
            SensorInfo si = st.getSensor_info();
            List<ValueInfo> lvi = si.getValue_info();
            for(ValueInfo vi: lvi){
                vi.setId(viBox.put(vi));
            }
            si.setValue_info(lvi);
            si.setId(siBox.put(si));
            st.setSensor_info(si);
            //Putting Timeseries
            tsBox.put(st);
            Log.d("DataAdapter","SensorTimeseriesSaved");
            }catch(Exception e){
                e.printStackTrace();
            }
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
    public void deleteTimeseries(SensorTimeseries st){
        Box stBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        Box seBox = Module.getBoxStore().boxFor(StringEntity.class);
        Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
        Box siBox = Module.getBoxStore().boxFor(SensorInfo.class);
        Box viBox = Module.getBoxStore().boxFor(ValueInfo.class);
        for(SensorValue sv:st.getValues()){
            for(StringEntity se: sv.getValues()){
                seBox.remove(se);
            }
            svBox.remove(sv);
        }
        for(ValueInfo vi: st.getSensor_info().getValue_info()){
            viBox.remove(vi);
        }
        siBox.remove(st.getSensor_info());
        stBox.remove(st);
    }

    public List<SensorTimeseries> getAllSensorTimeseries(){
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        return tsBox.getAll();
    }

    public List<SensorValue> getAllSensorValues(){
        Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
        return svBox.getAll();
    }

    public SensorTimeseries getSensorTimeseries(String sensorname, String timestampDay){
        long id = searchForTimeseriesOfSameDay(timestampDay, sensorname);
        Box stBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        return (SensorTimeseries) stBox.get(id);
    }
}
