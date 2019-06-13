package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class LocationDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);

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


//        ParseObject location = new ParseObject("Location");
//        gameScore.put("score", 17);
//        gameScore.put("playerName", "Saja from Location description");
//        gameScore.put("cheatMode", false);
//        location.saveInBackground();

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

        TextView t = (TextView) findViewById(R.id.activityTitleChat);
    }
}
