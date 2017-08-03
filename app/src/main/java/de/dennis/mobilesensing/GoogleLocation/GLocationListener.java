package de.dennis.mobilesensing.GoogleLocation;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * Created by Anton on 02.08.2017.
 */

public class GLocationListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude
    private String coordinates; //lat+long
    private GoogleApiClient mGAC;
    private Context mContext;
    public static final String TAG = "GPSresource";

    public GLocationListener(Context c)
    {
        mContext = c;
        try {
            buildGoogleApiClient();
            mGAC.connect();
        }
        catch(Exception e)
        {
            Log.d(TAG,e.toString());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGAC = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

    @Override
    public void onConnected(Bundle bundle) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(de.dennis.mobilesensing.Application.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( de.dennis.mobilesensing.Application.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
