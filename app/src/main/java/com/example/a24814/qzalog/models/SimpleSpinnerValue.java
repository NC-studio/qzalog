package com.example.a24814.qzalog.models;

/**
 * Created by 24814 on 11/19/2016.
 */
public class SimpleSpinnerValue {

    private Integer id;

    private String name;

    private Integer position;

    public SimpleSpinnerValue(){
        super();
    }


    public SimpleSpinnerValue(Integer id, String name, Integer position) {
        super();
        this.id = id;
        this.name = name;
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

}
