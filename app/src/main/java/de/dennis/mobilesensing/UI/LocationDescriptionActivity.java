package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import de.dennis.mobilesensing.models.Location;
import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class LocationDescriptionActivity extends AppCompatActivity {

    final static ArrayList<Location> locationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);

        final TextView t = (TextView) findViewById(R.id.activityTitleChat);

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
                Intent i = new Intent(Application.getContext(), MapsActivity.class);
                startActivity(i);
                finish();
            }
        });




//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
//        query.getInBackground("a6oNjMky4F", new GetCallback<ParseObject>() {
//            public void done(ParseObject locationObj, ParseException e) {
//                if (e == null) {
//                    // object will be your location
//                    Double latitude = locationObj.getDouble("Latitude");
//                    Double longitude = locationObj.getDouble("Longitude");
//                    String title = locationObj.getString("Title");
//                    String description = locationObj.getString("Description");
//                } else {
//                    try {
//                        throw new Exception();
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });

    }
}
