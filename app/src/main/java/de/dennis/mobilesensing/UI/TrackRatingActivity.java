package de.dennis.mobilesensing.UI;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBox.Track.TrackObject;
import de.dennis.mobilesensing_module.mobilesensing.Storage.ObjectBoxAdapter;


public class TrackRatingActivity extends AppCompatActivity implements OnMapReadyCallback{

    ArrayList<TrackObject> trackList;
    ImageButton imageButtonLeft;
    ImageButton imageButtonRight;
    FloatingActionButton floatingActionButtonCheck;
    MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_rating);
        Log.d("MAPVIEW","here");
        map = (MapView) findViewById(R.id.mapView);
        map.getMapAsync(this);
        imageButtonLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
        imageButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousTrack();
            }
        });
        imageButtonRight = (ImageButton) findViewById(R.id.imageButtonRight);
        imageButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTrack();
            }
        });
        floatingActionButtonCheck =  (FloatingActionButton) findViewById(R.id.floatingActionButtonCheck);
        floatingActionButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTrack();
            }
        });
    }

    private void updateTrack() {

    }

    private void nextTrack() {

    }

    private void previousTrack() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        ObjectBoxAdapter oba = new ObjectBoxAdapter();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("MAPVIEW","map ready");
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}
