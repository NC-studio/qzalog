package com.example.a24814.qzalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.models.Form;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FormFragment extends Fragment {

    private final String TAG = "FormFragment";

    private FrameLayout regionSpinner;

    private View view;

    private JSONObject formObject;

    private List<Form> fields = new ArrayList<Form>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.form,
                container, false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void initView(){
        regionSpinner = (FrameLayout) view.findViewById(R.id.regionSpinner);
        regionSpinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });

        formObject = ((FormActivity)getActivity()).getForm();

        Log.d("123", formObject.toString());

        try {
            JSONObject jObject = formObject.getJSONObject("fields");

            Log.d("123", jObject.toString());

            Iterator<String> iter = jObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
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

                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                }
            }
        }catch (Throwable t) {
            Log.d(TAG, t.getMessage());
        }




    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        JSONObject formRegion = ((BaseFile) getActivity().getApplication()).getFormRegion();
        try {
            String value = formRegion.getString("name");
            if(value != null){
                ((TextView) view.findViewById(R.id.regionText)).setText(value);
            }else{
                ((TextView) view.findViewById(R.id.regionText)).setText(getResources().getString(R.string.form_default));
            }
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }





}
