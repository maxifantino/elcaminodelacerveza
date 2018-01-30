package com.mgfdev.elcaminodelacerveza.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;

import java.util.List;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;

public class MapsActivity extends CustomFragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

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
        cacheHelper = CacheManagerHelper.getInstance();
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
        googleMap.setOnInfoWindowClickListener(this);

        List<BrewerInfo> brewersLocation = cacheHelper.getBrewers();
        renderMap(brewersLocation);
    }

    private void renderMap(List <BrewerInfo> brewersLocation){
        BitmapDescriptor beerIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_chop);

        for (BrewerInfo location:brewersLocation) {
            if (!location.isFiltered()){
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(location.getBrewery()).snippet("Ver detalle").icon(beerIcon));
            }
        }
        setCurrentLocationOnMap();
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(ctx,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void setCurrentLocationOnMap(){
        Location camaraLocation = locService.getLastKnownLocation();
        LatLng camaraLatLong;
        if (camaraLocation != null && camaraLocation.getLongitude() != 0.0d){
            camaraLatLong = new LatLng(camaraLocation.getLatitude(), camaraLocation.getLongitude());
        }
        else{
            camaraLatLong = new LatLng(-34.5931964f, -58.4479855f);
        }
        boolean hasAccess = (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) ||

                (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED);
        if (hasAccess) {
            googleMap.setMyLocationEnabled(true);
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        BrewerInfo brewer = cacheHelper.getBrewerById(marker.getTitle());
        Intent detailIntent = new Intent(getContext(), BrewerDetail.class);
        detailIntent.putExtra("Brewer", brewer);
        getContext().startActivity(detailIntent);
    }
}
