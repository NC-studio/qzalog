package com.example.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SelectedObjectsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        initFragment(savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView toolbarText = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarText.setText("Выбранные объекты");
    }


    private void initFragment(Bundle savedInstanceState){
        FrameLayout flContent = (FrameLayout) findViewById(R.id.flContent);
        if (savedInstanceState == null) {

            Fragment newFragment = new SelectedObjectsFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            // fragmentTransaction.replace(android.R.id.content, newFragment);

            fragmentTransaction.add(R.id.flContent, newFragment);

            fragmentTransaction.commit();
        }

    }

}
