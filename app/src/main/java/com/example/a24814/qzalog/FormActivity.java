package com.example.a24814.qzalog;

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
import com.example.a24814.qzalog.components.DataBaseAdapter;
import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Form;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    private Category category;

    private final String TAG = "FormActivity";

    private Integer categoryPosition;

    private JSONObject formObject;

    private Integer previusCategory;

    private List<Form> fields = new ArrayList<Form>();

    private View view;

    FrameLayout flContent;



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
            category = categories.get(categoryPosition);
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(category.getName());

            //dynamic
            DataBaseAdapter myDatabaseHelper = new DataBaseAdapter(this, true);
            formObject = myDatabaseHelper.getForm(category.getObjectId());
            myDatabaseHelper.close();

            //previusCategory = ((BaseFile) getApplication()).getCategoryId();
           // if(previusCategory == null || previusCategory != extras.getInt("category")){
              //  previusCategory =  extras.getInt("category");
               // ((BaseFile) getApplication()).setCategoryId( previusCategory);

           // }else{

            //}
        }


        fields = ((BaseFile) getApplication()).getFields();

        initFragment(savedInstanceState);


        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initFragment(Bundle savedInstanceState){
        flContent = (FrameLayout) findViewById(R.id.flContent);
        if (savedInstanceState == null) {
            Fragment newFragment = new FormFragment();

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
        MenuItem item = menu.findItem(R.id.action_cancel);
        if(item != null) {
            item.setVisible(true);
            //item.setActionView(R.layout.toolbar_textview);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel) {

            FormFragment fragment = (FormFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
            fragment.clearForm();


          /*  ViewGroup group = (ViewGroup)findViewById(R.id.flContent);
            Log.d("testio", String.valueOf(group.getChildCount()));

            ArrayList<EditText> EditTextList = new ArrayList<EditText>();

            for( int i = 0; i < flContent.getChildCount(); i++ ) {

                Log.d("testio", flContent.getChildAt(i).toString());
                if (flContent.getChildAt(i) instanceof EditText) {

                    ((EditText) flContent.getChildAt(i)).setText("");
                }

            }
*/


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public JSONObject getForm(){
        return formObject;
    }

    public Integer getCategoryId(){
        return category.getObjectId();
    }

    public List<Form> getFields() {
        return fields;
    }

    public Category getCategory() {
        return category;
    }

    /*public void setClearedHistory(Boolean clearedHistory){
        this.clearedHistory = clearedHistory;
    }

    public void setSended(Boolean sended){
        this.sended = sended;
    }

    public void setRegionSelecting(Boolean regionSelecting){
        this.regionSelecting = regionSelecting;
    }*/




    @Override
    public void onResume()
    {


        super.onResume();  // optional depending on your needs
    }

    @Override
    public void onBackPressed()
    {

        finish();
    }


}
