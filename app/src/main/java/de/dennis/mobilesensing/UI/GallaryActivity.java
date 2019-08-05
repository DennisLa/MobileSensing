package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing.models.ImageAdapter;

public class GallaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        Intent i = getIntent();
        final String locationName = i.getStringExtra("LocationName");
        final String locationDescription = i.getStringExtra("LocationDescription");
        final String searchInput = i.getStringExtra("searchInput");
        final double lat = i.getDoubleExtra("Latitude",0);
        final double lng = i.getDoubleExtra("Longitude", 0);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer image = (Integer) parent.getItemAtPosition(position);
                Intent i = new Intent(Application.getContext(), AddNewLocationActivity.class);
//                i.putParcelableArrayListExtra("List",  locationsList);
                i.putExtra("image", image);
                i.putExtra("LocationName", locationName);
                i.putExtra("LocationDescription", locationDescription);
                i.putExtra("Latitude", lat);
                i.putExtra("Longitude", lng);
                i.putExtra("searchInput", searchInput);
                startActivity(i);
                finish();
            }
        });
    }
}