package com.ncgoup.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.ncgoup.a24814.qzalog.components.BaseFile;

public class GalleryActivity extends AppCompatActivity {

    private Integer objId;

    private Integer position = 0;

    private Toolbar toolbar;

    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            objId = extras.getInt("objId");
            position = extras.getInt("position");
        }
        Integer imagesAmount = ((BaseFile) getApplication()).getImagesAmount();

        toolbarText = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarText.setText("Изображение " + String.valueOf(position+1) +  "/" + String.valueOf(imagesAmount));

    }

    private void initFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Fragment newFragment = new GalleryFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flContent, newFragment);
            fragmentTransaction.commit();
        }

    }

    public TextView getToolbarText(){
        return toolbarText;
    }

    public Integer getPosition(){
        return position;
    }

}

