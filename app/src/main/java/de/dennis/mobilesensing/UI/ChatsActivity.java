package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;
import de.dennis.mobilesensing.Application;
import de.dennis.mobilesensing.R;
import de.dennis.mobilesensing.models.ListViewAdapter;

public class ChatsActivity extends AppCompatActivity {

    private ArrayList<de.dennis.mobilesensing.models.Location> locationsList = new ArrayList<>();
    //change this to locations in database

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        menu.getItem(0).setChecked(false);//turn off the check on the first button of nav. bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.menu_maps:
                        i = new Intent(ChatsActivity.this, MapsActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_data:
                        i = new Intent(ChatsActivity.this, DataActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_chats:
                        break;
                }
                return true;
            }
        });

        addToolBar();
        readExistingLocations();
        createListView();

    }

    private void createListView() {
        listView = (ListView) findViewById(R.id.listView);
        // now create an adapter class

        String mTitle[] = new String[locationsList.size()];
        for (int i=0; i<locationsList.size(); i++) {
            mTitle[i] = locationsList.get(i).getTitle();
        }

        ListViewAdapter adapter = new ListViewAdapter(this, locationsList, mTitle);
        listView.setAdapter(adapter);


        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText(ChatsActivity.this, "Facebook Description", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // so item click is done now check list view
    }

    private void readExistingLocations() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        List<ParseObject> listOfLocationParseObj;
        try {
            listOfLocationParseObj = query.find();
            listOfLocationParseObj.get(0).getDouble("Latitude");
            for (int i=0; i<listOfLocationParseObj.size(); i++) {
                Double latitude = listOfLocationParseObj.get(i).getDouble("Latitude");
                Double longitude = listOfLocationParseObj.get(i).getDouble("Longitude");
                String title = listOfLocationParseObj.get(i).getString("Title");
                String description = listOfLocationParseObj.get(i).getString("Description");
                int image = listOfLocationParseObj.get(i).getInt("Photo");
                boolean addedSuccessfully = locationsList.add(
                        new de.dennis.mobilesensing.models.Location(new LatLng(latitude, longitude),
                                title,
                                description,
                                image));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addToolBar() {
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatActivity);
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
        Intent i = new Intent(Application.getContext(), MapsActivity.class);
        startActivity(i);
        finish();
    }
}
