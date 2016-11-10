package com.example.a24814.qzalog.models;

/**
 * Created by 24814 on 11/10/2016.
 */
public class Region {
    private Integer id;
    private String name;
    private Integer parent;
    private Integer position;

    public Region(){
        super();
    }


    public Region(Integer id, String name, Integer position, Integer parent) {
        super();
        this.id = id;
        this.name = name;
        this.position = position;
        this.parent = parent;
    }

    public String getName()
    {
        return name;
    }
    public Integer getId()
    {
        return id;
    }
    public Integer getParent()
    {
        return parent;
    }
    public Integer getPosition()
    {
        return position;
    }

}
