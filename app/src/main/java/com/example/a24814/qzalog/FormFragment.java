package com.example.a24814.qzalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.Defaults;
import com.example.a24814.qzalog.components.FromCreator;
import com.example.a24814.qzalog.models.Form;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FormFragment extends Fragment {

    private final String TAG = "FormFragment";

    private FrameLayout regionSpinner;

    private String regionId;

    private View view;

    private JSONObject formObject;

    private List<Form> fields = new ArrayList<Form>();

    private LinearLayout formFields;

    private RelativeLayout searchFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.form,
                container, false);
        initView();

        fields = ((FormActivity)getActivity()).getFields();

        if(fields.size() > 0){
            parseSavedList();
            Log.d("test", "test123");
        }else{
            parseJson();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void initView(){
        formFields = (LinearLayout)   view.findViewById(R.id.formFields);
        regionSpinner = (FrameLayout) view.findViewById(R.id.regionSpinner);
        regionSpinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });
        searchFooter = (RelativeLayout) view.findViewById(R.id.searchFooter);
        searchFooter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String request = generateUrlRequest();
                Log.d("test request", request);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        JSONObject formRegion = ((BaseFile) getActivity().getApplication()).getFormRegion();
        try {
            String regionIdValue = formRegion.getString("id");
            if(regionIdValue != null){
                regionId = regionIdValue;
            }
            String regionName = formRegion.getString("name");
            if(regionName != null){
                ((TextView) view.findViewById(R.id.regionText)).setText(regionName);
            }else{
                ((TextView) view.findViewById(R.id.regionText)).setText(getResources().getString(R.string.form_default));
            }
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }




    }

    /**
     * Get JsonObject from DB and parse it separate components (fields)
     * Save fields to list (fields)
     * Add view of each field to Form View
     */
    private void parseJson(){
        formObject = ((FormActivity)getActivity()).getForm();
        try {
            JSONObject jObject = formObject.getJSONObject("fields");

            Iterator<String> iter = jObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    /**
                     * Generating Form object from json Object
                     */
                    JSONObject value = jObject.getJSONObject(key);
                    Integer id = Integer.valueOf(key);
                    String unit_of_measure = value.getString("unit_of_measure");
                    String title = value.getString("title");
                    String placeholder = value.getString("placeholder");
                    String name = value.getString("name");
                    String name2 = value.getString("name2");
                    Integer type = value.getInt("type");
                    Integer main = value.getInt("main");
                    JSONObject jsonList = null;
                    if(type == 2){
                        jsonList = value.getJSONObject("values");
                    }
                    Form field = new Form(id, title, name, name2, unit_of_measure, placeholder, type, main, jsonList);
                    fields.add(field);
                    FromCreator fromCreator = new FromCreator(getActivity(), field);
                    View v = fromCreator.createField();
                    formFields.addView(v);
                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                }
            }
            ((BaseFile) getActivity().getApplication()).setFields(fields);
        }catch (Throwable t) {
            Log.d(TAG, t.getMessage());
        }
    }

    private void parseSavedList(){
        for (Form field : fields)
        {
            FromCreator fromCreator = new FromCreator(getActivity(), field);
            View v = fromCreator.createField();
            formFields.addView(v);
        }
    }

    private String generateUrlRequest(){
        String url = Defaults.CATEGORY_PATH;

        Integer category_id = ((FormActivity)getActivity()).getCategoryId();
        url = url + "?category="+String.valueOf(category_id);
        if(regionId != null){
            url = url + "&ObjectsSearch[region_id]=" + regionId;
        }

        return url;
    }


}
