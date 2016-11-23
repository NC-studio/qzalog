package com.ncgoup.a24814.qzalog.models;


import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class contains the parsed value of JSON description of field.
 */
public class Form {

    /**
     * Id of field
     */
    private Integer id;

    /**
     * String that used for showing unit measure of field
     */
    private String unit_of_measure;

    /**
     * Hint of EditText or Spinner
     */
    private String placeholder;

    /**
     * Description of field
     */
    private String title;

    /**
     * GET name of field. It used for formating GET REQUEST
     */
    private String name;

    /**
     * If a field is a rang field, we should use a second name
     */
    private String name2;

    /**
     * Not used
     */
   // private Integer main;

    /**
     * Type can equal to 1 or 2; 1 is a range field; 2 is a spinner
     */
    private Integer type;

    /**
     * Spinner values
     */
    private List<SimpleSpinnerValue> jsonList;

    /**
     * Map for detecting a key of selected view in spinner
     */
    private Map<Integer,String> values = new HashMap<>();

    /**
     * Selected value of spinner or value of first edittext
     */
    private String selectedValue;

    /**
     * Value of second edit text
     */
    private String selectedValue2;

    private Integer position;

    private View view;

    public Form(){
        super();
    }

    public Form(Integer id, String title, String name, String name2, String unit_of_measure, String placeholder, Integer type, List<SimpleSpinnerValue> jsonList, Integer position) {
        super();
        this.id = id;
        this.name = name;
        this.name2 = name2;
        this.title = title;
        this.unit_of_measure = unit_of_measure;
        this.placeholder = placeholder;
        this.type = type;
        //this.main = main;
        this.jsonList = jsonList;
        this.position = position;
    }

    public Integer getId()
    {
        return id;
    }

    public Integer getPosition()
    {
        return position;
    }

    public String getName()
    {
        return name;
    }

    public String getName2()
    {
        return name2;
    }

    public String getTitle()
    {
        return title;
    }

    public Integer getType()
    {
        return type;
    }

    public String getMeasure()
    {
        return unit_of_measure;
    }

    public List<SimpleSpinnerValue> getJsonList()
    {
        return jsonList;
    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public Map<Integer,String> getList()
    {
        return values;
    }

    public void setList(Map<Integer,String> fields)
    {
        this.values = fields;
    }

    public void setSelectedValue(String value)
    {
        this.selectedValue = value;
    }

    public String getSelectedValue()
    {
        return this.selectedValue;
    }

    public void setSelectedValue2(String value)
    {
        this.selectedValue2 = value;
    }

    public String getSelectedValue2()
    {
        return this.selectedValue2;
    }

    public void setView(View value)
    {
        this.view = value;
    }

    public View getView()
    {
        return this.view;
    }


}


