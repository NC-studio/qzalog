package com.example.a24814.qzalog.models;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MapObject implements ClusterItem {

    private Integer id;

    private String coordX;

    private String coordY;

    private LatLng mPosition;

    public MapObject(){
        super();
    }

    public MapObject(Integer id, String coordX, String coordY) {
        super();
        this.id = id;
        this.coordX = coordX;
        this.coordY = coordY;
        mPosition = new LatLng(Double.valueOf(coordX), Double.valueOf(coordY));
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

    public LatLng getPosition() {
        return mPosition;
    }
}
