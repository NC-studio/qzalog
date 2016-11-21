package com.example.a24814.qzalog.components;

import android.app.Application;

import com.example.a24814.qzalog.models.Category;
import com.example.a24814.qzalog.models.Form;
import com.example.a24814.qzalog.models.FormHistory;
import com.example.a24814.qzalog.models.Objects;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BaseFile extends Application {

    private String urlRequest = Defaults.CATEGORY_PATH;

    private Integer categoryId;

    private List<Form> fields = new ArrayList<Form>();

    private List<Category> cagegoryList = new ArrayList<Category>();

    private JSONObject formRegion = new JSONObject();

    private List<FormHistory> formHistory = new ArrayList<FormHistory>();

    private List<Objects> objectList = new ArrayList<Objects>();

    private Objects objectModel = null;

    private JSONObject images = new  JSONObject();

    private Integer imagesAmount = 1;

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

    public String getUrl(){
        return urlRequest;
    }

    public void setUrl(String url){
        this.urlRequest = url;
    }

    public List<FormHistory> getFormHistory(){
        return formHistory;
    }

    public void setFormHistory(){
        this.formHistory =  new ArrayList<FormHistory>();
        addToFormHistory();
    }

    public void addToFormHistory(){
        FormHistory formHistoryObj = new FormHistory(getUrl(), getFields(), getFormRegion());
        formHistory.add(formHistoryObj);
    }

    public JSONObject getImages() {
        return this.images;
    }

    public void setImages(JSONObject images) {
        this.images = images;
    }

    public Integer getImagesAmount() {
        return this.imagesAmount;
    }

    public void setImagesAmount(Integer imagesAmount) {
        this.imagesAmount = imagesAmount;
    }

    public Objects getObjectModel() {
        return this.objectModel;
    }

    public void setObjectModel(Objects objectModel) {
        this.objectModel = objectModel;
    }







}
