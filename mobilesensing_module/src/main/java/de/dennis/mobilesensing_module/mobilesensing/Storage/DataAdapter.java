package de.dennis.mobilesensing_module.mobilesensing.Storage;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.DoubleEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GeoPointEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.IntegerEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.LineStringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.StringEntity;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorInfo;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorTimeseries;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.SensorValue;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.ValueInfo;
import io.objectbox.Box;

/**
 * Created by Dennis on 05.08.2017.
 */

public class DataAdapter {
    public DataAdapter() {

    }

    public void saveTimeseriesToOB(SensorTimeseries st)
    {
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        SensorTimeseries st_day = getSensorTimeseries(st.getTimestamp_day(),st.getSensor_info().getSensor_name());

        //
        Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
        Box ieBox = Module.getBoxStore().boxFor(IntegerEntity.class);
        Box seBox = Module.getBoxStore().boxFor(StringEntity.class);
        Box gpeBox = Module.getBoxStore().boxFor(GeoPointEntity.class);
        Box deBox = Module.getBoxStore().boxFor(DoubleEntity.class);
        Box siBox = Module.getBoxStore().boxFor(SensorInfo.class);
        Box viBox = Module.getBoxStore().boxFor(ValueInfo.class);
        Box lseBox = Module.getBoxStore().boxFor(LineStringEntity.class);
        //
        if(st_day != null){
            //Update Entity

            //Putting SensorValue & ObjectEntities
            List<SensorValue> lsv = st.getValues();
            for(SensorValue sv: lsv){
                ArrayList<GeoPointEntity> gpe = new ArrayList<>();
                ArrayList<StringEntity> se = new ArrayList<>();
                ArrayList<IntegerEntity> ie = new ArrayList<>();
                ArrayList<DoubleEntity> de = new ArrayList<>();
                ArrayList<LineStringEntity> lse = new ArrayList<>();
                List<Object> lo = sv.getValues();
                for(Object o: lo){
                    if (o.getClass().equals(GeoPointEntity.class)){
                        ((GeoPointEntity)o).setId(gpeBox.put(o));
                    }
                    if(o.getClass().equals(StringEntity.class)){
                        ((StringEntity)o).setId(seBox.put(o));
                    }
                    if(o.getClass().equals(IntegerEntity.class)){
                        ((IntegerEntity)o).setId(ieBox.put(o));
                    }
                    if(o.getClass().equals(DoubleEntity.class)){
                        ((DoubleEntity)o).setId(deBox.put(o));
                    }
                    if(o.getClass().equals(LineStringEntity.class)){
                        LineStringEntity ls = (LineStringEntity)o;
                        for(GeoPointEntity gp: ls.getValues()){
                            gp.setId(gpeBox.put(gp));
                        }
                        ls.setId(lseBox.put(o));
                    }
                }
                sv.setGeoPointEntities(gpe);
                sv.setIntegerEntities(ie);
                sv.setStringEntities(se);
                sv.setDoubleEntities(de);
                sv.setLineStringEntities(lse);
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
            //Putting SensorValue & StringEntitiy
           List<SensorValue> lsv = st.getValues();
                for(SensorValue sv: lsv){
                    List<Object> lo = sv.getValues();
                    int i = 0;
                    ArrayList<GeoPointEntity> gpe = new ArrayList<>();
                    ArrayList<StringEntity> se = new ArrayList<>();
                    ArrayList<IntegerEntity> ie = new ArrayList<>();
                    ArrayList<DoubleEntity> de = new ArrayList<>();
                    ArrayList<LineStringEntity> lse = new ArrayList<>();
                    for(Object o: lo){
                        if (o.getClass().equals(GeoPointEntity.class)){
                            ((GeoPointEntity)o).setId(gpeBox.put(o));
                            gpe.add((GeoPointEntity)o);
                            i++;
                        }
                        if(o.getClass().equals(StringEntity.class)){
                            ((StringEntity)o).setId(seBox.put(o));
                            se.add((StringEntity)o);
                            i++;
                        }
                        if(o.getClass().equals(IntegerEntity.class)){
                            ((IntegerEntity)o).setId(ieBox.put(o));
                            ie.add((IntegerEntity)o);
                            i++;
                        }
                        if(o.getClass().equals(DoubleEntity.class)){
                            ((DoubleEntity)o).setId(deBox.put(o));
                        }
                        if(o.getClass().equals(LineStringEntity.class)){
                            LineStringEntity ls = (LineStringEntity)o;
                            for(GeoPointEntity gp: ls.getValues()){
                                gp.setId(gpeBox.put(gp));
                            }
                            ls.setId(lseBox.put(o));
                        }
                    }
                    Log.d("DataAdapter",i+"");
                    sv.setGeoPointEntities(gpe);
                    sv.setIntegerEntities(ie);
                    sv.setStringEntities(se);
                    sv.setDoubleEntities(de);
                    sv.setLineStringEntities(lse);
                    sv.setId(svBox.put(sv));
                }
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

    public SensorTimeseries getSensorTimeseries(String timestamp_day, String sensor_name ){
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        List<SensorTimeseries> lst = tsBox.getAll();
        SensorTimeseries st = null;
        for(SensorTimeseries sti: lst){
            if(sti.getTimestamp_day().equals(timestamp_day) && sti.getSensor_info().getSensor_name().equals(sensor_name)){
                st = sti;
                break;
            }
        }
        return st;
    }
    public List<SensorTimeseries> getSensorTimeseriesOlder(String timestamp_day, String sensor_name){
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        List<SensorTimeseries> lst = tsBox.getAll();
        ArrayList<SensorTimeseries> rlst = new ArrayList<>();
        for(SensorTimeseries st: lst){
            if(!(st.getTimestamp_day().equals(timestamp_day)) && st.getSensor_info().getSensor_name().equals(sensor_name)){
                rlst.add(st);
            }
        }
        return rlst;
    }
    public void deleteTimeseries(SensorTimeseries st){
        Box stBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        Box ieBox = Module.getBoxStore().boxFor(IntegerEntity.class);
        Box seBox = Module.getBoxStore().boxFor(StringEntity.class);
        Box gpeBox = Module.getBoxStore().boxFor(GeoPointEntity.class);
        Box svBox = Module.getBoxStore().boxFor(SensorValue.class);
        Box siBox = Module.getBoxStore().boxFor(SensorInfo.class);
        Box viBox = Module.getBoxStore().boxFor(ValueInfo.class);
        Box deBox = Module.getBoxStore().boxFor(DoubleEntity.class);
        Box lseBox = Module.getBoxStore().boxFor(LineStringEntity.class);
        for(SensorValue sv:st.getValues()){
            for(Object o: sv.getValues()){
                if (o.getClass().equals(GeoPointEntity.class)){
                    gpeBox.remove(o);
                }
                if(o.getClass().equals(StringEntity.class)){
                    seBox.remove(o);
                }
                if(o.getClass().equals(IntegerEntity.class)){
                    ieBox.remove(o);
                }
                if(o.getClass().equals(DoubleEntity.class)){
                    deBox.remove(o);
                }
                if(o.getClass().equals(LineStringEntity.class)){
                    LineStringEntity lse = (LineStringEntity)o;
                    for(GeoPointEntity gp: lse.getValues()){
                        gpeBox.remove(gp);
                    }
                    lseBox.remove(lse);
                }
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

    public void deleteTimeseries(String timestamp_day, String sensorName) {
        SensorTimeseries st = getSensorTimeseries(timestamp_day,sensorName);
        deleteTimeseries(st);
    }

    public void deleteAllTimeseries() {
        List<SensorTimeseries> lst = getAllSensorTimeseries();
        for(SensorTimeseries ts : lst){
            deleteTimeseries(ts.getTimestamp_day(),ts.getSensor_info().getSensor_name());
        }
    }

    public List<SensorTimeseries> getAllSensorTimeseriesOlder(String timestamp_day) {
        Box tsBox = Module.getBoxStore().boxFor(SensorTimeseries.class);
        List<SensorTimeseries> lst = tsBox.getAll();
        ArrayList<SensorTimeseries> rlst = new ArrayList<>();
        for(SensorTimeseries st: lst){
            if(!(st.getTimestamp_day().equals(timestamp_day))){
                rlst.add(st);
            }
        }
        return rlst;
    }
}
