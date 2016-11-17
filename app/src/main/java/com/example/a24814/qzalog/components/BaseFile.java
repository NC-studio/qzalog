package com.example.a24814.qzalog.components;

import android.app.Application;

import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Form;
import com.example.a24814.qzalog.models.Objects;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BaseFile extends Application {

    private List<Category> cagegoryList = new ArrayList<Category>();

    private JSONObject formValues;

    private JSONObject formRegion = new JSONObject();

    private List<Form> fields = new ArrayList<Form>();

    private Integer categoryId;

    private List<Objects> objectList = new ArrayList<Objects>();


    public List<Category> getCategories() {
        return this.cagegoryList;
    }

    public void setFormRegion(JSONObject formRegion) {
        this.formRegion = formRegion;
    }

    public JSONObject getFormRegion() {
        return this.formRegion;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Form> getFields() {
        return this.fields;
    }

    public void setFields(List<Form> fields) {
        this.fields = fields;
    }

    public List<Objects> getObjects() {
        return this.objectList;
    }

    public void setObjects(List<Objects> objectList) {
        this.objectList = objectList;
    }


}
