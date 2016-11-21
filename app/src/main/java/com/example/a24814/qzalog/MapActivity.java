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

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.models.Category;

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private Category category;

    private Integer categoryPosition;

    private String urlRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("Карта");


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            categoryPosition = extras.getInt("category");
            if(categoryPosition != null) {
                List<Category> categories = ((BaseFile) getApplication()).getCategories();
                category = categories.get(categoryPosition);
                ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(category.getName());
                urlRequest = ((BaseFile) getApplication()).getUrl() + "&map=1";
            }
        }


        initFragment(savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment(Bundle savedInstanceState){
        FrameLayout flContent = (FrameLayout) findViewById(R.id.flContent);
        if (savedInstanceState == null) {

            Fragment newFragment = new MapFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            // fragmentTransaction.replace(android.R.id.content, newFragment);

            fragmentTransaction.add(R.id.flContent, newFragment);

            fragmentTransaction.commit();
        }

    }

    public String getUrl(){
        return urlRequest;
    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
