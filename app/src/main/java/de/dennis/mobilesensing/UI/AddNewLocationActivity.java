package de.dennis.mobilesensing.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    LatLng latLng = new LatLng(52.2779659, 7.9853896);
    double lat, lng =0 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        final ImageView btnSaveNewLocation = (ImageView) findViewById(R.id.btnSaveLocation);
        final Button btnGallary = (Button) findViewById(R.id.btnOpenGalary);
        final Button LocationSearch = (Button) findViewById(R.id.search_button);
        final EditText locationNameEditText = (EditText) findViewById(R.id.LocationName);
        final EditText descriptionEditText = (EditText) findViewById(R.id.description);
        final EditText searchInput = (EditText) findViewById(R.id.input_search);

        Intent i = getIntent();
        final int image = i.getIntExtra("image", R.drawable.a_pic_default);
        String locationName = i.getStringExtra("LocationName");
        String locationDescription = i.getStringExtra("LocationDescription");
        String searchString = i.getStringExtra("searchInput");
        lat = i.getDoubleExtra("Latitude", 52.2779659); // osnabruck coordination
        lng = i.getDoubleExtra("Longitude", 7.9853896);

        if (locationName != null) locationNameEditText.setText(locationName);
        if (locationDescription != null) descriptionEditText.setText(locationDescription);
        if (searchString != null) {
            searchInput.setText(searchString);
            LocationSearch.post(new Runnable(){
                @Override
                public void run() {
                    LocationSearch.performClick();
                }
            });
        }
        if (locationName != null || locationDescription !=  null || searchString != null)
        {
            // if the picture is already choosen, change the color of the gallary button, and the text
            btnGallary.setText("Foto wurde erfolgreich ausgewählt");
            btnGallary.setBackgroundResource(R.drawable.roundgreenbutton);
        }

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
                    location.put("Photo", image);
                    location.saveInBackground();
                    goBack();
                }
            }
        });

        btnGallary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Application.getContext(), GallaryActivity.class);
                i.putExtra("LocationName", locationNameEditText.getText().toString());
                i.putExtra("LocationDescription", descriptionEditText.getText().toString());
                i.putExtra("Latitude", latLng.latitude);
                i.putExtra("Longitude", latLng.longitude);
                i.putExtra("searchInput", searchInput.getText().toString());
                startActivity(i);
                finish();
            }
        });

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        LatLng latLng = new LatLng(lat,lng);
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
                Drawable personPin = getResources().getDrawable(R.drawable.map_marker);
                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(personPin);
                mMap.addMarker(new MarkerOptions().position(latLng).icon(markerIcon));
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
