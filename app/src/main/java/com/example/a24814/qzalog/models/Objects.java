package com.example.a24814.qzalog.models;


import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Objects{

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }


    private Integer id;

    private String name;

    private String url;

    private String region;

    private String price;

    private String discount;

    private String info;


    private String complex;

    private String dateCreated;

    private JSONObject phones;

    private JSONObject images;

    private JSONObject infoArray;

    private String description;

    private int visibility= View.GONE;

    private String coordX;

    private String coordY;

    private String zoom;

    private Drawable likerIcon;

    private Boolean liked;

    public Objects(){
        super();
    }

    public Objects(Integer id, String name, String url, String region, String price, String discount, String info, Drawable likerIcon, Boolean liked) {
        super();
        this.id = id;
        this.name = name;
        this.url = url;
        this.region = region;
        this.price = price;
        this.discount = discount;
        this.info = info;
        if(!this.discount.isEmpty()){
            visibility = View.VISIBLE;
        }
        this.likerIcon = likerIcon;
        this.liked = liked;



    }

    public Objects(Integer id, String name, String complex, String dateCreated, String address, String price, String discount, JSONObject phones, JSONObject images, JSONObject infoArray, String description, String coordX, String coordY, String zoom, Boolean liked) {
        super();
        this.id = id;
        this.name = name;
        this.complex = complex;
        this.dateCreated = dateCreated;
        this.price = price;
        this.discount = discount;
        this.region = address;

        this.phones = phones;
        this.images = images;
        this.infoArray = infoArray;
        this.description = description;

        this.coordX = coordX;
        this.coordY = coordY;
        this.zoom = zoom;
        this.liked = liked;
    }

    public @Visibility int getVisibility() {
        return visibility;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getImgUrl()
    {
        return url;
    }

    public String getRegion()
    {
        return region;
    }

    public String getPrice()
    {
        return price;
    }

    public String getDiscount()
    {
        return discount;
    }

    public String getInfo()
    {
        return info;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public String getComplex()
    {
        return complex;
    }

    public JSONObject getInfoJson()
    {
        return infoArray;
    }

    public JSONObject getPhones()
    {
        return phones;
    }

    public JSONObject getImages()
    {
        return images;
    }


    public String getDscription()
    {
        return description;
    }

    public String getCoordX()
    {
        return coordX;
    }

    public String getCoordY()
    {
        return coordY;
    }

    public String getZoom()
    {
        return zoom;
    }

    public Drawable getLikerIcon(){
        return likerIcon;
    }

    public void setLikerIcon(Drawable icon){
         this.likerIcon = icon;
    }

    public Boolean getLiked(){
        return liked;
    }

    public void setLiked(Boolean liked){
        this.liked = liked;
    }







}
