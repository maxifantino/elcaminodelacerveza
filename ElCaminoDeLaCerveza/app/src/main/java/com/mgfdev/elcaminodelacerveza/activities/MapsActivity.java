package com.mgfdev.elcaminodelacerveza.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import java.util.List;
import com.mgfdev.elcaminodelacerveza.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String userName;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        userName = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // get all brewers location;
        WordpressApiService apiService = new WordpressApiService();
        List<BeerLocation> brewersLocation = apiService.getBeerLocations(userName,password);

        for (BeerLocation location:brewersLocation) {
            LatLng currentLatLng = new LatLng(location.getLattitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title(location.getCraftName()));
        }
        LatLng camaraLatLong = new LatLng(34.364, 58.223);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camaraLatLong));

    }
}
