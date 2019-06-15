package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class AddNewLocationActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private static final String TAG = "MapActivity";
    private EditText mSearchText;
    LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        final Button btnSaveNewLocation = (Button) findViewById(R.id.btnSaveLocation);
        final EditText locationNameEditText = (EditText) findViewById(R.id.LocationName);
        final EditText descriptionEditText = (EditText) findViewById(R.id.description);

        btnSaveNewLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String locationName = locationNameEditText.getText().toString(); //gets you the contents of edit text
                String description = descriptionEditText.getText().toString(); //gets you the contents of edit text

                if(latLng == null) {
                    Toast.makeText(Application.getContext(), "Bitte wählen Sie einen Ort!", Toast.LENGTH_LONG).show();
                } else {
                    ParseObject location = new ParseObject("Location");
                    location.put("Title", locationName);
                    location.put("Description", description);
                    location.put("Latitude", latLng.latitude);
                    location.put("Longitude", latLng.longitude);
                    location.saveInBackground();
                    goBack();
                }
            }
        });

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                goBack();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.locationDescMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //osnabruck coordination
        LatLng latLng = new LatLng(52.2779659,7.9853896);
        moveCamera(latLng, 9);
    }

    public void goBack(){
        Intent i = new Intent(Application.getContext(), MapsActivity.class);
        startActivity(i);
        finish();
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.input_search);
        String location = locationSearch.getText().toString();
        List<Address> addressList = new ArrayList<>();

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size() > 0) {
                Address address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                moveCamera(latLng, 17);
            }
        }
    }

    public void moveCamera (LatLng latLng, int scale) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(scale).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
