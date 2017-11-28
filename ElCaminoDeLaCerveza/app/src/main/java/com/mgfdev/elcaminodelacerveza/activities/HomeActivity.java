package com.mgfdev.elcaminodelacerveza.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class HomeActivity extends FragmentActivity{

    private LocalizationService localizationService;
    private User user;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            View layout;
            boolean result = false;
            setAllLayoutsInvisible();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    layout = findViewById(R.id.mapsLayout);
                    layout.setVisibility(View.VISIBLE);
                    result= true; break;
                case R.id.navigation_passport:
                    layout = findViewById(R.id.passportLayout);
                    layout.setVisibility(View.VISIBLE);
                    result= true; break;
                case R.id.navigation_settings:
                    layout = findViewById(R.id.settingsLayout);
                    layout.setVisibility(View.VISIBLE);
                    result= true; break;
            }
            return result;
        }
    };

    public User getUser(){
        return user;
    }

    private void setAllLayoutsInvisible(){
        View layoutSeetings = findViewById(R.id.settingsLayout);
        layoutSeetings.setVisibility(View.INVISIBLE);
        View mapsLayout = findViewById(R.id.mapsLayout);
        mapsLayout.setVisibility(View.INVISIBLE);
        View passportLayout = findViewById(R.id.passportLayout);
        passportLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = (User) getIntent().getSerializableExtra("USER");
   //     setupLocalizationService();
    }

    private void setupLocalizationService(){
        localizationService = LocalizationService.getInstance(this.getApplicationContext(), user);
        localizationService.init();
    }
}