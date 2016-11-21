package com.example.a24814.qzalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a24814.qzalog.components.Backend;
import com.example.a24814.qzalog.components.BackendCallback;
import com.example.a24814.qzalog.components.BaseFile;
import com.example.a24814.qzalog.models.MapObject;
import com.example.a24814.qzalog.models.Objects;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapFragment extends Fragment {

    private MapFragment fragment;
    private View view;

    private GoogleMap googleMap;

    private MapView mMapView;

    private Objects object;

    private String url = null;

    private BackendCallback<List<MapObject>> backendAsync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment = this;
        view = inflater.inflate(R.layout.activity_map,
                container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        url = ((MapActivity)getActivity()).getUrl();
        if(url != null){
            backendAsync =  Backend.getMapObjects(getActivity(), fragment, url);
            backendAsync.execute();
        }else{
            object = ((BaseFile) getActivity().getApplication()).getObjectModel();
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                            /*try {
                                MapsInitializer.initialize(getActivity().getApplicationContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/

                    // For showing a move to my location button
                    try {
                        googleMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {

                    }

                    UiSettings settings = googleMap.getUiSettings();
                    settings.setZoomControlsEnabled(true);
                    settings.setZoomGesturesEnabled(true);

                    LatLng location = new LatLng(Double.valueOf(object.getCoordX()), Double.valueOf(object.getCoordY()));
                    googleMap.addMarker(new MarkerOptions().position(location));

                    // googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"));
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(Integer.valueOf(object.getZoom())).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }
        return view;
    }

    public void backendResponse(List<MapObject> mapObjects){

    }


    @Override
    public void onResume() {
        super.onResume();
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(backendAsync!=null){
            backendAsync.cancel(true);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/




}
