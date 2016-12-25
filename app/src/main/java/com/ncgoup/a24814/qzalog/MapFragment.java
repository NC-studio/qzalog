package com.ncgoup.a24814.qzalog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.ncgoup.a24814.qzalog.components.Backend;
import com.ncgoup.a24814.qzalog.components.BackendCallback;
import com.ncgoup.a24814.qzalog.components.BaseFile;
import com.ncgoup.a24814.qzalog.models.MapObject;
import com.ncgoup.a24814.qzalog.models.MapProperty;
import com.ncgoup.a24814.qzalog.models.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MapFragment extends Fragment {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;

    private MapFragment fragment;

    private View view;

    private GoogleMap googleMap;

    private MapView mMapView;

    private Objects object;

    private String url = null;

    private BackendCallback<List<MapObject>> backendAsync;

    private MapProperty mapProperty;

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
        if (servicesOK()) {
            if (url != null) {
                backendAsync = Backend.getMapObjects(getActivity(), fragment, url);
                backendAsync.execute();
            } else {
                object = ((BaseFile) getActivity().getApplication()).getObjectModel();

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        googleMap = mMap;
                        UiSettings settings = googleMap.getUiSettings();
                        settings.setZoomControlsEnabled(true);
                        settings.setZoomGesturesEnabled(true);
                        LatLng location = new LatLng(Double.valueOf(object.getCoordX()), Double.valueOf(object.getCoordY()));
                        googleMap.addMarker(new MarkerOptions().position(location));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(Integer.valueOf(object.getZoom())).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                });
            }
        }
        return view;
    }

    public void backendResponse(final List<MapObject> mapObjects){

        mapProperty = ((BaseFile) getActivity().getApplication()).geMapProperty();
        if (!mapObjects.isEmpty()) {
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

        if(googleMap != null){
            // Position the map.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapProperty.getPosition(), Integer.valueOf(mapProperty.getZoom())));

            // Initialize the manager with the context and the map.
            // (Activity extends context, so we can pass 'this' in the constructor.)
            mClusterManager = new ClusterManager<MapObject>(getActivity(), googleMap);
            mClusterManager.setOnClusterItemClickListener(mClusterItemClickListener);
            mClusterManager.setOnClusterClickListener(mClusterItemClickListenerList);

            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            googleMap.setOnCameraIdleListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);


            // Add cluster items (markers) to the cluster manager.
            addItems(mapObjects);

        }

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

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
                    getActivity(), GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Can not connect!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



}
