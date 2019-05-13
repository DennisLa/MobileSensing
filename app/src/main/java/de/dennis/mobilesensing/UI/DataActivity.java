package de.dennis.mobilesensing.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.dennis.mobilesensing.R;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        TextView t = (TextView) findViewById(R.id.activityTitleData);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        menu.getItem(0).setChecked(false);//turn off the check on the first button of nav. bar


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
    }
}
