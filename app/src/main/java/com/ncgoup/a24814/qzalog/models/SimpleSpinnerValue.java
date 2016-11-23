package com.ncgoup.a24814.qzalog.models;

/**
 * Created by 24814 on 11/19/2016.
 */
public class SimpleSpinnerValue {

    private String id;

    private String name;

    private Integer position;

    public SimpleSpinnerValue(){
        super();
    }


    public SimpleSpinnerValue(String id, String name, Integer position) {
        super();
        this.id = id;
        this.name = name;
        this.position = position;


    }
    public String getId()
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

}
