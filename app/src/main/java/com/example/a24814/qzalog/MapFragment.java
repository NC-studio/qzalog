package com.example.a24814.qzalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MapFragment extends Fragment {

    private MapFragment fragment;

    private View view;

    private GoogleMap googleMap;

    private MapView mMapView;

    private Objects object;

    private String url = null;

    private BackendCallback<List<MapObject>> backendAsync;

    // Declare a variable for the cluster manager.
    private ClusterManager<MapObject> mClusterManager;

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
            Log.d("testtest", url);
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
                   /* try {
                        googleMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                    }*/

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

    public void backendResponse(final List<MapObject> mapObjects){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                UiSettings settings = googleMap.getUiSettings();
                settings.setZoomControlsEnabled(true);
                settings.setZoomGesturesEnabled(true);

                setUpClusterer(mapObjects);
            }
        });

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


    private void setUpClusterer(List<MapObject> mapObjects) {

        // Position the map.
        MapObject mapObjectFirst = mapObjects.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapObjectFirst.getPosition(), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MapObject>(getActivity(), googleMap);
        mClusterManager.setOnClusterItemClickListener(mClusterItemClickListener);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems(mapObjects);
    }

    private void addItems(List<MapObject> mapObjects) {

        //Add ten cluster items
        for (MapObject mapObject : mapObjects) {
            mClusterManager.addItem(mapObject);
        }



    }

    public ClusterManager.OnClusterItemClickListener<MapObject> mClusterItemClickListener = new ClusterManager.OnClusterItemClickListener<MapObject>() {

        @Override
        public boolean onClusterItemClick(MapObject item) {
            Intent intent = new Intent(getActivity(), ObjectDetailActivity.class);
            intent.putExtra("objId", item.getId());
            startActivity(intent);

            return true;
        }
    };

    public ClusterManager.OnClusterClickListener mClusterItemClickListenerList = new ClusterManager.OnClusterClickListener<MapObject>(){

        @Override
        public boolean onClusterClick(Cluster<MapObject> cluster) {
            Collection items = cluster.getItems();


            List<Integer> selectedObjects = new ArrayList<Integer>();
            for (Object item : items) {
                Integer id = ((MapObject) item).getId();
                selectedObjects.add(id);
            }
            ((BaseFile) getActivity().getApplication()).setMapObjects(selectedObjects);

            Intent intent = new Intent(getActivity(), SelectedObjectsActivity.class);
            startActivity(intent);


            return false;
        }
    };



}
