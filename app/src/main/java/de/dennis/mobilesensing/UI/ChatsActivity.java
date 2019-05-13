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

public class ChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        TextView t = (TextView) findViewById(R.id.activityTitleChat);

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
    }
}
