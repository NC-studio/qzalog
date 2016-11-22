package com.example.a24814.qzalog.models;

/**
 * Created by 24814 on 11/19/2016.
 */
public class SimpleValueOrder {


    private String name;

    private String value;

    private Integer position;

    public SimpleValueOrder(){
        super();
    }

    public SimpleValueOrder(String name, String value,Integer position) {
        super();

        this.name = name;
        this.value = value;
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

    public String getValue()
    {
        return value;
    }

}
