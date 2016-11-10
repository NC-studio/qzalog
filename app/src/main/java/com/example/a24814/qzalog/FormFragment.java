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

import org.json.JSONException;
import org.json.JSONObject;


public class FormFragment extends Fragment {

    private final String TAG = "FormFragment";

    private FrameLayout regionSpinner;

    private final String DEFAULT_VALUE = "Не важно";


    private View view;


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



    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        JSONObject formRegion = ((BaseFile) getActivity().getApplication()).getFormRegion();
        Log.d("testio REGION", String.valueOf(formRegion));
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
