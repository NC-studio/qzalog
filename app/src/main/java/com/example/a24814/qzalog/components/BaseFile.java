package com.example.a24814.qzalog.components;

import android.app.Application;

import com.example.a24814.qzalog.models.Category;

import java.util.ArrayList;
import java.util.List;


public class BaseFile extends Application {


    private List<Category> cagegoryList = new ArrayList<Category>();

    public List<Category> getCategories() {
        return this.cagegoryList;
    }




}
