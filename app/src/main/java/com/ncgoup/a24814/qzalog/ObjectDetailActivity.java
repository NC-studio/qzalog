package com.ncgoup.a24814.qzalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.components.DataBaseAdapter;
import com.ncgoup.a24814.qzalog.models.Objects;

import java.util.List;

public class ObjectDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final String TAG = "ObjectDetailActivity";


    private Context _context;

    private Integer objId;

    private MenuItem mapIcon;

    private MenuItem likeIcon;

    private Integer objPos;

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
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("Объявление");

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
            objPos = extras.getInt("objPos");
        }


    }

    private void initFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Fragment newFragment = new ObjectDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.flContent, newFragment);
            fragmentTransaction.commit();
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        mapIcon = menu.findItem(R.id.action_map);
        likeIcon = menu.findItem(R.id.action_like);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_like) {
            try {
                Objects object = ((BaseFile) getApplication()).getObjectModel();
                DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(this, true);
                Boolean liked = myDatabaseHelper.manageLiked(object.getId());
                myDatabaseHelper.close();
                if (liked) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_star_active));
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_star));
                }
                object.setLiked(liked);

                //Не уверен что критично важно и сильно помогает делу
                ((BaseFile) getApplication()).setObjectModel(object);
                List<Objects> objects = ((BaseFile) getApplication()).getObjects();
                Objects objectSelected = objects.get(objPos);
                objectSelected.setLiked(liked);
                if (liked) {
                    objectSelected.setLikerIcon(getResources().getDrawable(R.drawable.ic_liked_active));
                } else {
                    objectSelected.setLikerIcon(getResources().getDrawable(R.drawable.ic_liked));
                }

            }catch(Exception e){
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public Integer getObjId(){
        return objId;
    }

    public MenuItem getMapIcon(){
        return mapIcon;
    }

    public MenuItem getLikeIcon(){
        return likeIcon;
    }


}
