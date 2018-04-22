package de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import de.dennis.mobilesensing_module.mobilesensing.EventBus.SensorDataEvent;
import de.dennis.mobilesensing_module.mobilesensing.Module;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.GLocation.GLocationsObject;
import de.ms.ptenabler.locationtools.ObjectboxPtenablerUtilities;


/**
 * Created by Anton on 02.08.2017.
 */

public class GLocationListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {


    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude
    private String coordinates, coordinatesUpdate; //lat+long
    private GoogleApiClient mGAC;
    private Context mContext;
    public static final String TAG = "GPSresource";
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private int stopLoopint; // 1 is active 0 is deactivated
    private boolean mRequestingLocationUpdates=true;

    public GLocationListener(Context c, long interval, long fastestInterval)
    {
        mContext = c;
        try {
            buildGoogleApiClient();
            mGAC.connect();
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Module.getContext());
        }
        catch(Exception e)
        {
            Log.d(TAG,e.toString());
        }
        createLocationCallback();
        createLocationRequest(interval,fastestInterval);
    }





    protected synchronized void buildGoogleApiClient() {
        mGAC = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnected(Bundle bundle) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(Module.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Module.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGAC);

    }
    @SuppressLint("RestrictedApi")
    protected void createLocationRequest(long interval, long fastestInterval) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void createLocationCallback(){
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    coordinatesUpdate = location.getLatitude() +","+ location.getLongitude();
                    //EventBus.getDefault().post(new MessageEvent(coordinates));
                    Log.d(TAG, coordinatesUpdate);
                    //New GLocationObject*****************************************************************
                    GLocationsObject glo = new GLocationsObject(System.currentTimeMillis(),location.getLatitude(),location.getLongitude(), location.getSpeed());
                    //Send Event
                    ObjectboxPtenablerUtilities.checkLocationInCluster(glo);
                    SharedPreferences.Editor edit  = Module.getContext().getSharedPreferences("LastLocation", Context.MODE_PRIVATE).edit();
                    edit.putFloat("LastLat", (float) location.getLatitude());
                    edit.putFloat("LastLng", (float) location.getLongitude());
                    edit.apply();
                    EventBus.getDefault().post(new SensorDataEvent(glo));
                    //**********************************************************************************
                    SharedPreferences prefsdata = Module.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefsdata.edit();
                    editor.putString("Location",coordinatesUpdate);
                    editor.apply();
                    Log.d("ObjectBox_Location","updated to"+coordinatesUpdate);
                }
            };
        };
    }

    public void startLocationUpdates() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(Module.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Module.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }


        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public String getCoordinates(){

        if(location != null){
            coordinates = location.getLatitude() +","+ location.getLongitude();
            //EventBus.getDefault().post(new MessageEvent(coordinates));
            Log.d(TAG, coordinates);
        }
        return coordinates;

    }


    public void stopLocationUpdates() {

            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }



    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
