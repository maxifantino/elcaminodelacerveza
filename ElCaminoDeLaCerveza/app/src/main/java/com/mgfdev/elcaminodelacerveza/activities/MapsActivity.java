package com.mgfdev.elcaminodelacerveza.activities;

import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import java.util.List;
import com.mgfdev.elcaminodelacerveza.R;

public class MapsActivity extends CustomFragment{

    private GoogleMap googleMap;
    private MapView mMapView;
    private CacheManagerHelper cacheHelper;

    public static MapsActivity newInstance (){
        MapsActivity fragment = new MapsActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void configGmap(View rootView, Bundle savedInstanceState){
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                drawMap(mMap);
            }
        });
    }

    private void drawMap(GoogleMap gMap) {
        googleMap = gMap;
        // get all brewers location;
        CacheManagerHelper cacheHelper = CacheManagerHelper.getInstance();
        List<BeerLocation> brewersLocation = cacheHelper.getBrewers();
        renderMap(brewersLocation);
    }

    private void renderMap(List <BeerLocation> brewersLocation){
        BitmapDescriptor beerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_chop);

        for (BeerLocation location:brewersLocation) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(location.getBrewery()).icon(beerIcon));
        }
        LatLng camaraLatLong = new LatLng(-34.364f, -58.223f);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camaraLatLong, 4.0f));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        configGmap(rootView, savedInstanceState);
        return rootView;
    }
}
