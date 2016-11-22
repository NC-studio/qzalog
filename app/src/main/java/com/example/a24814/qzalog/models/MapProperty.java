package com.example.a24814.qzalog.models;

import com.google.android.gms.maps.model.LatLng;

public class MapProperty {

    private String coordX;

    private String coordY;

    private String zoom;

    private LatLng mPosition;

    public MapProperty(){
        super();
    }

    public MapProperty(String coordX, String coordY, String zoom) {
        super();
        this.zoom = zoom;
        this.coordX = coordX;
        this.coordY = coordY;
        mPosition = new LatLng(Double.valueOf(coordX), Double.valueOf(coordY));

    }

    public String getCoordX()
    {
        return coordX;
    }

    public String getCoordY()
    {
        return coordY;
    }

    public String getZoom() {
        return zoom;
    }

    public LatLng getPosition() {
        return mPosition;
    }
}
