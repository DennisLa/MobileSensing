package de.dennis.mobilesensing.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import de.dennis.mobilesensing.R;

public class LocationDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);
        TextView t = (TextView) findViewById(R.id.activityTitleChat);
    }
}
