package com.example.a24814.qzalog.models;

/**
 * Created by 24814 on 11/19/2016.
 */
public class SimpleImageModel {


    private String name;

    private Integer position;

    public SimpleImageModel(){
        super();
    }

    public SimpleImageModel(String name, Integer position) {
        super();

        this.name = name;
        this.position = position;


    }

    public Integer getPosition()
    {
        return position;
    }

    public String getName()
    {
        return name;
    }

}
