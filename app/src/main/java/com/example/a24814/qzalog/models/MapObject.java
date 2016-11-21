package com.example.a24814.qzalog.models;


public class MapObject {

    private Integer id;

    private String coordX;

    private String coordY;

    public MapObject(){
        super();
    }

    public MapObject(Integer id, String coordX, String coordY) {
        super();
        this.id = id;
        this.coordX = coordX;
        this.coordY = coordY;

    }

    public Integer getId()
    {
        return id;
    }

    public String getCoordX()
    {
        return coordX;
    }

    public String getCoordY()
    {
        return coordY;
    }
}
