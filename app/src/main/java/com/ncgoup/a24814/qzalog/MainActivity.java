package com.ncgoup.a24814.qzalog;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.a24814.qzalog.R;
import com.ncgoup.a24814.qzalog.components.DataBaseAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final String TAG = "MainActivity";

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    //Frame view for fragments
    FrameLayout flContent;

    private DataBaseAdapter dbHelper;

    private  Toolbar toolbar;

    private DrawerLayout drawer;

    private ActionBarDrawerToggle toggle;

    private Activity activity;

    private NavigationView n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,  R.string.drawer_open,  R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //initDataBase();
        initFragment(savedInstanceState);

        //init data base
        initDataBase();

        //init goole map
        Runnable r = new Runnable() {
            public void run() {
                try {
                    MapsInitializer.initialize(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MapView mapView = new MapView(MainActivity.this);
                mapView.onCreate(null);
            }
        };



        n = (NavigationView) findViewById(R.id.nav_view);
        n.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.nav_liked:
                        intent = new Intent(activity, LikedObjectsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_about:
                        intent = new Intent(activity, InfoActivity.class);
                        startActivity(intent);
                        return true;
                }
                drawer.closeDrawers();
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        n.getMenu().getItem(0).setChecked(true);
    }



    private void initFragment(Bundle savedInstanceState){
        flContent = (FrameLayout) findViewById(R.id.flContent);
        if (savedInstanceState == null) {
            Fragment newFragment = new CategoryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flContent, newFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        return true;
    }

    private void initDataBase(){
        dbHelper = new DataBaseAdapter(this, false);
    }

}
