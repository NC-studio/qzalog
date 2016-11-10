package com.example.a24814.qzalog.components;

import android.app.Application;

import com.example.a24814.qzalog.models.Category;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BaseFile extends Application {

    private List<Category> cagegoryList = new ArrayList<Category>();

    private JSONObject formValues;

    private JSONObject formRegion = new JSONObject();




    public List<Category> getCategories() {
        return this.cagegoryList;
    }

    public void setFormRegion(JSONObject formRegion) {
        this.formRegion = formRegion;
    }

    public JSONObject getFormRegion() {
        return this.formRegion;
    }


}
