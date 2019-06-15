package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing.models.ClusterMarker;
import de.dennis.mobilesensing.util.ClusterManagerRenderer;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private LocationManager locationManager;
    private LocationListener locationListener;
    List <Marker> markerList;
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private ArrayList<de.dennis.mobilesensing.models.Location> locationsList = new ArrayList<>();
    //change this to locations in database
    private static final String TAG = "MapsActivity";
    List<ParseObject> listOfLocationParseObj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        ParseObject gameScore = new ParseObject("User");
//        gameScore.put("password", "1337");
//        gameScore.put("email", "saja@gmail.com");
//        gameScore.saveInBackground();

        FloatingActionButton fab = findViewById(R.id.add_new_location);
        fab.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               Intent i = new Intent(Application.getContext(), AddNewLocationActivity.class);
               startActivity(i);
               finish();
//               setContentView(R.layout.location_description);
           }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.menu_maps:
                        break;
                    case R.id.menu_data:
                        i = new Intent(MapsActivity.this, DataActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_chats:
                        i = new Intent(MapsActivity.this, ChatsActivity.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

//        markerList = new ArrayList<>();
//        Drawable personPin = getResources().getDrawable(R.drawable.map_marker);
//        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(personPin);
//        Marker mZoo = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(52.249906, 8.070234))
//                .icon(markerIcon));
//        markerList.add(mZoo);
//        Marker mSportPark = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(52.255764, 8.066930))
//                .icon(markerIcon));
//        markerList.add(mSportPark);
//
//        for (Marker m : markerList){
//            LatLng latLng = new LatLng((m.getPosition()).latitude, m.getPosition().longitude);
//            //mMap.addMarker(new MarkerOptions().position(latLng));

        markerList = new ArrayList<>();
//        locationsList.add(new de.dennis.mobilesensing.models.Location(
//                new LatLng(52.249906, 8.070234), "ZOO", ""));
//        locationsList.add(new de.dennis.mobilesensing.models.Location(
//                new LatLng(52.255764, 8.066930), "pARK", ""));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        try {
            listOfLocationParseObj = query.find();
            listOfLocationParseObj.get(0).getDouble("Latitude");
            for (int i=0; i<listOfLocationParseObj.size(); i++) {
                Double latitude = listOfLocationParseObj.get(i).getDouble("Latitude");
                Double longitude = listOfLocationParseObj.get(i).getDouble("Longitude");
                String title = listOfLocationParseObj.get(i).getString("Title");
                String description = listOfLocationParseObj.get(i).getString("Description");
                boolean addedSuccessfully = locationsList.add(
                        new de.dennis.mobilesensing.models.Location(new LatLng(latitude, longitude),
                                title,
                                description));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addMapMarkers();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    private void addMapMarkers() {

        if (mMap != null) {

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(this.getApplicationContext(), mMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new ClusterManagerRenderer(
                        this,
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for (de.dennis.mobilesensing.models.Location mLocation : locationsList) {
                Log.i(TAG, "addMapMarkers: location: ");
                try {
                    String snippet = "is it working?";
                    int avatar = R.drawable.logo; // set the default avatar
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            mLocation.getPosition(),
                            //new LatLng(52.249906, 8.070234),
                            "title",
                            snippet,
                            avatar
                    );
                    mClusterMarkers.add(newClusterMarker);
                    mClusterManager.addItem(newClusterMarker);
                }

                catch (NullPointerException e) {
                    Log.e(TAG, "addMapMarkers: NullPointerException: " );
                    e.printStackTrace();
                }
            }
            mClusterManager.cluster();
//            setCameraView();
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
        }
    }


    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);

            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        Drawable personPin = getResources().getDrawable(R.drawable.map_marker);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(personPin);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .title("User Current Location")
                .icon(markerIcon);
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        CameraUpdate loc = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//        mMap.animateCamera(loc);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder( this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                .zoom(17).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Call Activity after 5s = 5000ms
                Intent i = new Intent(Application.getContext(), LocationDescriptionActivity.class);
//                i.putParcelableArrayListExtra("List",  locationsList);
                i.putExtra("latitude", marker.getPosition().latitude);
                i.putExtra("longitude", marker.getPosition().longitude);
                for (int k =0 ; k<locationsList.size(); k++){
                    if (locationsList.get(k).getPosition().latitude ==  marker.getPosition().latitude
                            && locationsList.get(k).getPosition().longitude ==  marker.getPosition().longitude) {
                        i.putExtra("title", locationsList.get(k).getTitle());
                        i.putExtra("description", locationsList.get(k).getDescription());
                    }
                }

//                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        }, 2000);
        return true;
    }
}
