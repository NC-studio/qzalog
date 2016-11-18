package com.example.a24814.qzalog.components;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a24814.qzalog.R;
import com.example.a24814.qzalog.models.Form;
import com.example.a24814.qzalog.models.SimpleSpinnerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Used for creating and controling of fields in form Fragment
 */
public class FromCreator {


    private Form field;

    private Context context;

    private LayoutInflater inflater;

    public FromCreator(){
        super();
    }

    public FromCreator(Context context, Form field) {
        super();
        this.field = field;
        this.context = context;
    }

    /**
     * Cteates a view with field (EditTexts or Spinner) for form view
     */
    public View createField(){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.field_title, null);

        /**
         * Set title of view
         */
        ((TextView) v.findViewById(R.id.titleField)).setText(field.getTitle());

        /**
         * Create view and add it to frameLayout in current view
         */
        FrameLayout frame = (FrameLayout)  v.findViewById(R.id.dynamicField);
        if(field.getType() == 1){
            View v1 = createInput();
            frame.addView(v1);
        }else if(field.getType() == 2){
            View v1 = createSpinner();
            frame.addView(v1);
        }
        return v;
    }

    /**
     * Create EditTexts
     */
    private View createInput(){
        View v =  inflater.inflate(R.layout.field_type_1, null);
        /**
         * Set unit of measure of edit texts
         */
        ((TextView) v.findViewById(R.id.measure)).setText(field.getMeasure());

        EditText editText1 = (EditText) v.findViewById(R.id.field1);
        if(field.getSelectedValue() != null){
            editText1.setText(field.getSelectedValue());
        }

        EditText editText2 = (EditText) v.findViewById(R.id.field2);
        if(field.getSelectedValue2() != null){
            editText2.setText(field.getSelectedValue2());
        }

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                field.setSelectedValue(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                field.setSelectedValue2(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });



        return v;
    }

    /**
     * Creates Spinner and declares it events
     */
    private View createSpinner(){
        View v1 =  inflater.inflate(R.layout.field_type_2, null);

        Spinner spinner = (Spinner) v1.findViewById(R.id.spinnerForm);

        /**
         * The values of this spinner
         */
        List<SimpleSpinnerValue> spinner_list = field.getJsonList();

        /**
         * Creates and fills array for spinner, alse it creates Hash map for select Events
         */
        Map<Integer,Integer> spinnerValues = new HashMap<>();
        ArrayList<String> spinnerlist = new ArrayList<String>();
        spinnerlist.add(field.getPlaceholder());
        Integer i = 1;
        Integer selectedPosition = 0;


        for (SimpleSpinnerValue spinner_value : spinner_list) {
            String key = String.valueOf(spinner_value.getId());
            spinnerValues.put(Integer.valueOf(i), Integer.valueOf(key));
            spinnerlist.add(spinner_value.getName());

            if(field.getSelectedValue() != null && field.getSelectedValue().equals(String.valueOf(key))){
                selectedPosition = i;

            }
            i = i + 1;
        }
        spinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
                spinnerlist));
             /*
            * Set selected value of spinner
             */
        spinner.setSelection(selectedPosition);
        //Helpers.selectSpinnerValue(spinner, field.getSelectedValue());

        /**
         * Save Hash Map to Form object for using it in select events
         */
        field.setList(spinnerValues);

        /**
         * Select events
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position > 0){
                    int key = field.getList().get(position);
                    field.setSelectedValue(String.valueOf(key));
                }else{
                    field.setSelectedValue(null);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        return v1;
    }
}
