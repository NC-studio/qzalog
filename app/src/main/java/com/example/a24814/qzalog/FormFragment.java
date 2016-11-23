package com.example.a24814.qzalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.components.Defaults;
import com.example.a24814.qzalog.components.FromCreator;
import com.example.a24814.qzalog.models.Form;
import com.example.a24814.qzalog.models.FormHistory;
import com.example.a24814.qzalog.models.Objects;
import com.example.a24814.qzalog.models.SimpleSpinnerValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private Boolean created = false;

    private Boolean sended = false;

    private Boolean clearedHistory = false;

    private Boolean regionSelecting = false;

    private Boolean generated = false;

    private Integer previusCategory;

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
        formFields = (LinearLayout)   view.findViewById(R.id.formFields);
        regionSpinner = (FrameLayout) view.findViewById(R.id.regionSpinner);
        regionSpinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                regionSelecting =true;

                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });
        searchFooter = (RelativeLayout) view.findViewById(R.id.searchFooter);
        searchFooter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String request = generateUrlRequest();

                ((BaseFile) getActivity().getApplication()).setUrl(request);
                ((BaseFile) getActivity().getApplication()).setObjects(new ArrayList<Objects>());
               // ((BaseFile) getActivity().getApplication()).setPage(1);

                sended = true;
                clearedHistory = false;

                ((BaseFile) getActivity().getApplication()).addToFormHistory();
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("category", ((FormActivity)getActivity()).getCategoryPosition());
                startActivity(intent);


               // getActivity().finish();
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
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

        if(((FormActivity)getActivity()).getCategoryChanged() == false){
            fields = ((BaseFile) getActivity().getApplication()).getFields();
        }

        if(fields.size() > 0){
            if(generated == false){
                fields = ((BaseFile) getActivity().getApplication()).getFields();
                parseSavedList();
            }
        }else{
            parseJson();
        }
        generated = true;


        if(regionSelecting != true && created == true) {
            if(clearedHistory == false) {
                clearedHistory = true;


                List<FormHistory> formHistory = ((BaseFile) getActivity().getApplication()).getFormHistory();
                if (formHistory.size() > 1) {
                    FormHistory formHistoryObj = formHistory.get(formHistory.size() - 2);
                    String request = formHistoryObj.getUrl();
                    List<Form> fields = formHistoryObj.getFields();
                    JSONObject region = formHistoryObj.getRegion();

                    ((BaseFile) getActivity().getApplication()).setFields(fields);
                    ((BaseFile) getActivity().getApplication()).setUrl(request);
                    ((BaseFile) getActivity().getApplication()).setFormRegion(region);

                  //  ((BaseFile) getActivity().getApplication()).setPage(1);
                    ((BaseFile) getActivity().getApplication()).setObjects(new ArrayList<Objects>());
                    formHistory.remove(formHistory.size() - 1);
                }
            }


        }else{
            regionSelecting = false;
            created = true;
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
            //get JSON OBJECT throw name 'fields'
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
                    //Integer main = value.getInt("main");
                    Integer position = value.getInt("position");

                    //Declare List of spinner values
                    List<SimpleSpinnerValue> spinner_values = null;
                    JSONObject jsonList = null;
                    if(type == 2){
                        //Get JSON object of values
                        jsonList = value.getJSONObject("values");

                        //iterate jsonObject of spinner values and fill List for spinner
                        Iterator<String> iterInner = jsonList.keys();
                        spinner_values = new ArrayList<SimpleSpinnerValue>();
                        while (iterInner.hasNext()) {
                            String keyInner = iterInner.next();
                            JSONObject valueInner = jsonList.getJSONObject(keyInner);


                            SimpleSpinnerValue simpleSpinnerValue = new SimpleSpinnerValue(valueInner.getString("id"), valueInner.getString("name"), Integer.valueOf(keyInner));
                            spinner_values.add(simpleSpinnerValue);
                        }

                        //Sort spinner values by position
                        Collections.sort(spinner_values, new Comparator<SimpleSpinnerValue>(){
                            public int compare(SimpleSpinnerValue emp1, SimpleSpinnerValue emp2) {
                                return Integer.valueOf(emp1.getPosition()).compareTo(emp2.getPosition());
                            }
                        });
                    }

                    Form field = new Form(id, title, name, name2, unit_of_measure, placeholder, type, spinner_values, position);
                    fields.add(field);

                } catch (JSONException e) {
                    Log.d(TAG, e.toString());
                }
            }
            Collections.sort(fields, new Comparator<Form>(){
                public int compare(Form emp1, Form emp2) {
                    // ## Ascending order
                    //return id2.compareToIgnoreCase(id1); // To compare string values
                     return Integer.valueOf(emp1.getPosition()).compareTo(emp2.getPosition()); // To compare integer values

                    // ## Descending order
                    // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
                     //return Integer.valueOf(emp2.getPosition()).compareTo(emp1.getPosition()); // To compare integer values
                }
            });

            for (Form field : fields) {

                FromCreator fromCreator = new FromCreator(getActivity(), field);
                View v = fromCreator.createField();
                formFields.addView(v);
                field.setView(v);
            }

            ((BaseFile) getActivity().getApplication()).setFields(fields);
        }catch (Exception e) {

        }
    }

    private void parseSavedList(){
        for (Form field : fields)
        {

            FromCreator fromCreator = new FromCreator(getActivity(), field);
            View v = fromCreator.createField();
            formFields.addView(v);
            field.setView(v);
        }
    }

    private String generateUrlRequest(){
        String url = Defaults.CATEGORY_PATH;
        Integer category_id = ((FormActivity)getActivity()).getCategoryId();
        url = url + "?category="+String.valueOf(category_id);
        url = url + "&ObjectsSearch[region_id]=";
        if(regionId != null){
            url = url +  regionId;
        }
        for (Form field : fields) {
           if(field.getType() == 1){
               if(field.getSelectedValue() != null){
                   url = url + "&" + field.getName() + "=" + field.getSelectedValue();
               }
               if(field.getSelectedValue2() != null){
                   url = url + "&" + field.getName2() + "=" + field.getSelectedValue2();
               }
           }else{
               if(field.getType() == 2){
                   if(field.getSelectedValue() != null){
                       url = url + "&" + field.getName() + "=" + field.getSelectedValue();
                   }
               }
           }
        }
        return url;
    }

    public void clearForm(){
        for (Form field : fields) {
            if(field.getSelectedValue() != null){
                View v = field.getView();
                field.setSelectedValue(null);
                if(field.getType() == 1){
                    ((EditText) v.findViewById(R.id.field1)).setText("");
                    ((EditText) v.findViewById(R.id.field2)).setText("");
                }else{
                    ((Spinner) v.findViewById(R.id.spinnerForm)).setSelection(0);
                }
            }
        }

        JSONObject formRegion = ((BaseFile) getActivity().getApplication()).getFormRegion();
        try {
            String regionIdValue = formRegion.getString("id");
            if (regionIdValue != null) {
                formRegion = new JSONObject();
                ((BaseFile) getActivity().getApplication()).setFormRegion(formRegion);
                regionId = null;
                ((TextView) view.findViewById(R.id.regionText)).setText(getResources().getString(R.string.form_default));
            }

        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }


    }


}
