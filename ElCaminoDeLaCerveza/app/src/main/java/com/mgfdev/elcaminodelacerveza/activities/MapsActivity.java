package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.location.Location;
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
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;

import java.util.List;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;

public class MapsActivity extends CustomFragment{

    private GoogleMap googleMap;
    private MapView mMapView;
    private CacheManagerHelper cacheHelper;
    private Context ctx;
    private LocalizationService locService;
    public static MapsActivity newInstance (){
        MapsActivity fragment = new MapsActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getContext();
        locService = LocalizationService.getInstance(getActivity());
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
        List<BrewerInfo> brewersLocation = cacheHelper.getBrewers();
        renderMap(brewersLocation);
    }

    private void renderMap(List <BrewerInfo> brewersLocation){
        BitmapDescriptor beerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_chop);

        for (BrewerInfo location:brewersLocation) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(location.getBrewery()).icon(beerIcon));
        }

        Location camaraLocation = locService.getLastKnownLocation();
        LatLng camaraLatLong;
        if (camaraLocation != null && camaraLocation.getLongitude() != 0.0d){
            camaraLatLong = new LatLng(camaraLocation.getLatitude(), camaraLocation.getLongitude());
        }
        else{
            camaraLatLong = new LatLng(-34.5931964f, -58.4479855f);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camaraLatLong, 11.0f));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        FontHelper.overrideFonts(ctx, container
                , "montserrat.ttf");

        configGmap(rootView, savedInstanceState);
        return rootView;
    }
}
