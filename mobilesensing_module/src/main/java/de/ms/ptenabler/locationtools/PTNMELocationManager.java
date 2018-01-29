package de.ms.ptenabler.locationtools;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import de.ms.ptenabler.Message.LocationMessage;
import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.LocationUtils;
import de.schildbach.pte.dto.Location;

public class PTNMELocationManager {

    private  LocationService service;
    private static PTNMELocationManager manager;
    private Location lastLocation;
    private  SharedPreferences prefs;
    private  android.os.Handler handler;
    public  int LocationServiceStatusCode;
    public  EventBus eventBus;
    public static final String LAST_LAT = "llat";
    public static final String LAST_LON = "llon";
    public static final String LAST_LOCTS = "llocts";

    public  LocationService getService() {
        return service;
    }
    public  void setService(LocationService svc) {
        if(svc ==null && service !=null) Toast.makeText(service, "TNME Service stopped", Toast.LENGTH_SHORT).show();
        service = svc;
    }

    private PTNMELocationManager(){
        manager = this;
        lastLocation = null;
        handler = new android.os.Handler();
        eventBus = EventBus.getDefault();
        LocationServiceStatusCode = ConnectionResult.SUCCESS;
        prefs= PreferenceManager.getDefaultSharedPreferences(Utilities.getContext());
    }
    public static PTNMELocationManager getManager(){
        if(manager!=null)return manager;
        return new PTNMELocationManager();
    }
    
    public  void startService(){
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP){
            if(!Dexter.isRequestOngoing()){
                Dexter.checkPermissions(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Utilities.getContext().startService(new Intent(Utilities.getContext(), LocationService.class));
                        }else{
                            setService(null);
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }else{
            Utilities.getContext().startService(new Intent(Utilities.getContext(), LocationService.class));
        }



    }

    public  void forceLocationUpdate(boolean enable) {
        if (service != null) {
            if (enable) {
                service.forceLocationUpdateSetting();

            } else {
                service.setNormalInterval();
            }

        }
    }

    public  Location fromLatLng(double lat, double lng){

        Location loc1;

        loc1 = Location.coord((int)(lat*1000000), (int)(lng*1000000));

        return loc1;
    }

    public  Location getLocation(){
        LocationMessage lm= eventBus.getStickyEvent(LocationMessage.class);
        if(lm!=null){
            return lm.loc;
        }else{
            int lat = prefs.getInt(LAST_LAT, 35000000);
            int lon = prefs.getInt(LAST_LON,40000000);
            return Location.coord(lat,lon);
        }
    }

    public  Location fromLatLng(android.location.Location loc){
        return fromLatLng(loc.getLatitude(), loc.getLongitude());
    }

    public  double[] getLocationLatLng(){
        Location x;
        x = getLocation();
        return new double[]{((double)x.lat)/1000000.0,((double)x.lon)/1000000.0};
    }

    public  LatLng getLatLng(Location loc){
        return new LatLng(((double)loc.lat)/1000000.0,((double)loc.lon)/1000000.0);
    }

    public  void clearHistory(){
        //TODO
        //Utilities.openDBConnection().clearSearchHistory();
    }

    public  double computeDistance(Location loc1, Location loc2){
        return computeDistance(((double) (loc1.lat)) / 1000000, ((double) (loc1.lon)) / 1000000, ((double) (loc2.lat)) / 1000000, ((double) (loc2.lon)) / 1000000);
    }

    public  double computeDistance(Location loc1, double lat2, double lon2){
        return computeDistance(((double)(loc1.lat))/1000000,((double)(loc1.lon))/1000000, lat2,lon2);
    }

    public  double computeDistance(double lat1,double lon1, double lat2, double lon2){
        return LocationUtils.computeDistance(lat1, lon1, lat2, lon2);
    }

    public  double computeDistance(android.location.Location loc1,android.location.Location loc2){
        return computeDistance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
    }

}
