package com.example.zsoltvarga.hydrate;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;

public class Achievements extends AppCompatActivity {
    private ArrayList<Badge> arrayOfBadges = new ArrayList<>();
    private ArrayList<ImageView> arrayOfBadgeImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //sets toolbar to be the actionbar
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        ActionBar theActionBar =  getSupportActionBar();
        theActionBar.setDisplayHomeAsUpEnabled(true);
        theActionBar.setTitle("Badges");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //toolbar methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.user_details:
                Intent startUserDetails = new Intent(this, UserDetails.class);
                startActivity(startUserDetails);
                return true;
            case R.id.charts:
                Intent startCharts = new Intent(this, Charts.class);
                startActivity(startCharts);
                return true;
            case R.id.settings:
                Intent startSettings = new Intent(this, Settings.class);
                startActivity(startSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
