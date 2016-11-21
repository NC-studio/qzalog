package com.example.a24814.qzalog;

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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.Defaults;
import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Objects;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    Category category;

    private final String TAG = "CategoryActivity";

    private Integer categoryPosition;

    private Integer previusCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            categoryPosition = extras.getInt("category");
            List<Category> categories = ((BaseFile) getApplication()).getCategories();
            category = categories.get(extras.getInt("category"));
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(category.getName());

            previusCategory = ((BaseFile) getApplication()).getCategoryId();
            if(previusCategory == null || previusCategory != category.getObjectId()){
                ((BaseFile) getApplication()).setCategoryId( category.getObjectId());
                String url = Defaults.CATEGORY_PATH + "?category="+String.valueOf(category.getObjectId());
               // ((BaseFile) getApplication()).setPage(1);
                ((BaseFile) getApplication()).setUrl(url);
                ((BaseFile) getApplication()).setObjects(new ArrayList<Objects>());

                ((BaseFile) getApplication()).setFormHistory();

            }
        }
        initFragment(savedInstanceState);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {


        super.onResume();
    }




    private void initFragment(Bundle savedInstanceState){
        FrameLayout flContent = (FrameLayout) findViewById(R.id.flContent);

        if (savedInstanceState == null) {

            Fragment newFragment = new CategoryObjectsFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            // fragmentTransaction.replace(android.R.id.content, newFragment);
            fragmentTransaction.add(R.id.flContent, newFragment);
            fragmentTransaction.commit();

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        if(item != null) {
            item.setVisible(true);

            //DrawableCompat.setTint(item.getIcon(), ContextCompat.getColor(this, R.color.white));


        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_search) {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra("categoryId", categoryPosition);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
