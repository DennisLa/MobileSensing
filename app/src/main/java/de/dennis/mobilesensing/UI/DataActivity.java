package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import androidx.annotation.NonNull;
import de.dennis.mobilesensing.R;

public class DataActivity extends AppCompatActivity implements OnMapReadyCallback {


    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        menu.getItem(0).setChecked(false);//turn off the check on the first button of nav. bar


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.menu_maps:
                        i = new Intent(DataActivity.this, MapsActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_data:
                        break;
                    case R.id.menu_chats:
                        i = new Intent(DataActivity.this, ChatsActivity.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dataActivityMap);
        mapFragment.getMapAsync(this);
    }



    private void addHeatMap() {

        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
//
//    private void readExistingLocations() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
//        List<ParseObject> listOfLocationParseObj;
//        try {
//            listOfLocationParseObj = query.find();
//            listOfLocationParseObj.get(0).getDouble("Latitude");
//            for (int i=0; i<listOfLocationParseObj.size(); i++) {
//                Double latitude = listOfLocationParseObj.get(i).getDouble("Latitude");
//                Double longitude = listOfLocationParseObj.get(i).getDouble("Longitude");
//                String title = listOfLocationParseObj.get(i).getString("Title");
//                String description = listOfLocationParseObj.get(i).getString("Description");
//                int image = listOfLocationParseObj.get(i).getInt("Photo");
//                boolean addedSuccessfully = locationsList.add(
//                        new de.dennis.mobilesensing.models.Location(new LatLng(latitude, longitude),
//                                title,
//                                description,
//                                image));
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }


    private ArrayList<LatLng> readItems() throws JSONException, ParseException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SensingUpload");
        query.whereEqualTo("name", "location");
        List<ParseObject> listOfActivity = query.find();
        int size = listOfActivity.size();
        for (int i = 0; i<size; i++) {
            Object parseObject = listOfActivity.get(i).get("values");
            String jsonString = parseObject.toString();
            JSONArray jarray = new JSONArray(jsonString);
            for (int j =0 ; j<jarray.length(); j++){
                JSONObject wholeJObj = jarray.getJSONObject(j);
                JSONArray subarray = wholeJObj.getJSONArray("value");
                wholeJObj = subarray.getJSONObject(0);
                JSONObject geometry = wholeJObj.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                double lng = Double.parseDouble(coordinates.get(0).toString());
                double lat = Double.parseDouble(coordinates.get(1).toString());
                list.add(new LatLng(lat, lng));
            }
        }
        return list;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addHeatMap();
    }
}
