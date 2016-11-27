package com.ncgoup.a24814.qzalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.a24814.qzalog.R;

public class ListActivity extends AppCompatActivity {

    private Integer parent;

    public static Integer numberOfPage = 1;

    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("Выбор региона");

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            numberOfPage = extras.getInt("numberOfPage") + 1;

        }else{
            numberOfPage = 1;
        }

        initFragment(savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfPage == 2){
                    Intent intent = new Intent(_context, ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    private void initFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Fragment newFragment = new RegionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flContent, newFragment);
            fragmentTransaction.commit();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(numberOfPage == 2){
            Intent intent = new Intent(this, ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

        finish();
    }


}
