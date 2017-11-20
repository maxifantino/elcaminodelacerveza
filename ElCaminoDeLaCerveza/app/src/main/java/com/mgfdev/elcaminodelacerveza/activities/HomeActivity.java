package com.mgfdev.elcaminodelacerveza.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mTextMessage;
    private GoogleMap mMap;

    private LocalizationService localizationService;
    private User user;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = (User) getIntent().getSerializableExtra("USER");

   //     setupLocalizationService();
    }

    private void setupLocalizationService(){
        localizationService = LocalizationService.getInstance(this.getApplicationContext(), user);
        localizationService.init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // get all brewers location;
        WordpressApiService apiService = new WordpressApiService();
        List<BeerLocation> brewersLocation = apiService.getBeerLocations();

        for (BeerLocation location:brewersLocation) {
            LatLng currentLatLng = new LatLng(location.getLattitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title(location.getCraftName()));
        }
        LatLng camaraLatLong = new LatLng(34.364, 58.223);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camaraLatLong));

    }
}
