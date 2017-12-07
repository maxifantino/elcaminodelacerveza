package com.mgfdev.elcaminodelacerveza.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.SharedPreferenceManager;

public class HomeActivity extends FragmentActivity implements ActionObserver{

    private User user;
    private SharedPreferenceManager sharedPreferences;

    private LocalizationService localizationService;
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
        if (user == null){
            user = (User) getIntent().getSerializableExtra("USER");
        }
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
        FontHelper.overrideFonts(this, findViewById(android.R.id.content)
                , "montserrat.ttf");

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        user = (User) getIntent().getSerializableExtra("USER");
        sharedPreferences = SharedPreferenceManager.getInstance(this);
        setLocationUpdates(getLocationSwitch());
    }


    private boolean getLocationSwitch(){
        return Boolean.parseBoolean(sharedPreferences.getStringValue("location"));
    }

    @Override
    public void setLocationUpdates(Boolean activate) {
        localizationService = LocalizationService.getInstance(this);
        if (activate) {
            localizationService.init();
            localizationService.getLocationUpdates();
        }
        else{
            localizationService.stop();
        }
    }
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }
}
