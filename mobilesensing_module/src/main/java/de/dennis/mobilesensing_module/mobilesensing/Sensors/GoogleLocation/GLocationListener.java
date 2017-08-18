package de.dennis.mobilesensing_module.mobilesensing.Sensors.GoogleLocation;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import de.dennis.mobilesensing_module.mobilesensing.Module;


/**
 * Created by Anton on 02.08.2017.
 */

public class GLocationListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


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

    public GLocationListener(Context c)
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
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    coordinatesUpdate = location.getLatitude() +","+ location.getLongitude();
                    //EventBus.getDefault().post(new MessageEvent(coordinates));
                    Log.d(TAG, coordinatesUpdate);
                }
            };
        };
    }




    protected synchronized void buildGoogleApiClient() {
        mGAC = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    public void getCoordinates(){
        if(location != null){
            coordinates = location.getLatitude() +","+ location.getLongitude();
            //EventBus.getDefault().post(new MessageEvent(coordinates));
            Log.d(TAG, coordinates);
        }

    }
    public void getUpdateCoordinates(int delay){
        stopLoopint =1;
        Handler handler = new Handler();


                while (stopLoopint != 0)

                {
                    handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    if (location != null) {
                        coordinates = location.getLatitude() + "," + location.getLongitude();
                        //EventBus.getDefault().post(new MessageEvent(coordinates));
                        Log.d(TAG, coordinates);

                    }
                }
                    },delay);
                }


    }

    public void stopLoop() {
        stopLoopint = 0;
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

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
