package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;

public class LocationChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chat);
        addToolBar();
    }

    private void addToolBar() {
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_locationChat);
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
    }

    public void goBack(){
        Intent i = new Intent(Application.getContext(), ChatsActivity.class);
        startActivity(i);
        finish();
    }

}
