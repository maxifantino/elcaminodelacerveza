package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.AndroidCheckHelper;
import com.mgfdev.elcaminodelacerveza.helpers.AppConstants;
import com.mgfdev.elcaminodelacerveza.helpers.BottomNavigationViewHelper;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.helpers.MessageDialogHelper;
import com.mgfdev.elcaminodelacerveza.services.BrewerGeofencesService;
import com.mgfdev.elcaminodelacerveza.services.CustomCommand;
import com.mgfdev.elcaminodelacerveza.services.GeofenceTransitionsIntentService;
import com.mgfdev.elcaminodelacerveza.services.GeofenceWrapperService;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.SharedPreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static java.lang.Thread.sleep;

public class HomeActivity extends FragmentActivity implements ActionObserver{

    private User user;
    private SharedPreferenceManager sharedPreferences;
    private LocalizationService localizationService;
    private Context ctx;
    private Activity activity;
    private CustomFragment passportObserver;
    private CustomObserver settingsObserver;
    private GoogleApiClient mGoogleApiClient;
    private List<BrewerInfo> brewers;
    private LocationRequest mLocationRequest;
    private PendingIntent mGeofencePendingIntent;
   // private GeofencingClient mGeofencingClient;
   // private LocationClient locationClient;
    private BrewerGeofencesService geofencesService;
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
                    result = true;
                    break;
                case R.id.navigation_passport:
                    layout = findViewById(R.id.passportLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
                case R.id.navigation_brewers:
                    layout = findViewById(R.id.brewersLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
                case R.id.navigation_settings:
                    layout = findViewById(R.id.settingsLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
            }
            return result;
        }
    };

    public void attachPassportObserver(CustomFragment observer){
        this.passportObserver = observer;
    }

    public void attachSettingsObserver(CustomObserver observer){
        this.settingsObserver = observer;
    }

    public User getUser() {
        if (user == null) {
            user = (User) getIntent().getSerializableExtra("USER");
        }
        return user;
    }

    private void setAllLayoutsInvisible() {
        View layoutSeetings = findViewById(R.id.settingsLayout);
        layoutSeetings.setVisibility(View.INVISIBLE);
        View mapsLayout = findViewById(R.id.mapsLayout);
        mapsLayout.setVisibility(View.INVISIBLE);
        View brewersLayout = findViewById(R.id.brewersLayout);
        brewersLayout.setVisibility(View.INVISIBLE);
        View passportLayout = findViewById(R.id.passportLayout);
        passportLayout.setVisibility(View.INVISIBLE);
        //LocationServices.GeofencingApi.addGeofences()
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return ;
        }
        FontHelper.overrideFonts(this, findViewById(android.R.id.content)
                , "montserrat.ttf");

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        ctx = this.getApplicationContext();
        user = (User) getIntent().getSerializableExtra("USER");
        sharedPreferences = SharedPreferenceManager.getInstance(this);
        brewers = CacheManagerHelper.getInstance().getBrewers();
        setAllLayoutsInvisible();
        View layout = findViewById(R.id.mapsLayout);
        layout.setVisibility(View.VISIBLE);
        activity = this;
        setLocationUpdates(getLocationSwitch());

    }

  /*  private void buildApiClient(){
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resp == ConnectionResult.SUCCESS) {
            locationClient = new GeofencingClient(ctx);
            locationClient.connect();
        }
    }
*/
    private boolean getLocationSwitch() {
        return Boolean.parseBoolean(sharedPreferences.getStringValue("location"));
    }

    @Override
    public void setLocationUpdates(Boolean activate) {
        Log.i("Location", "Location status: " + activate);
        localizationService = LocalizationService.getInstance(this);
        localizationService.init();
        geofencesService = BrewerGeofencesService.getInstance(this, brewers);

        if (!getLocationSwitch()){
            showYesNoDialog(this, getString(R.string.activateLocationTitle), getString(R.string.activateLocationDescription),
                    getString(R.string.activate), getString(R.string.cancel));
        }
        if (activate) {
            boolean result = localizationService.isLocationActive();
            if (!result) {
                showYesNoDialog(this, getString(R.string.activateLocationTitle), getString(R.string.activateLocationDescription),
                        getString(R.string.activate), getString(R.string.cancel));
            }
            else{
                geofencesService.startGeofencing();
            }
        } else {
            localizationService.stop();
            geofencesService.stopGeofencing();
        }
    }

    private void showYesNoDialog(final Context ctx, String title, String description, String yesText, String noText) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ctx);
        dlgAlert.setMessage(description);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton(yesText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstants.LOCATION_SETTINGS_REQUEST_CODE);
            }
        });

        dlgAlert.setNegativeButton(noText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setLocationUpdates(false);
                settingsObserver.notifyEvent(AppConstants.SETTINGS_LOCATION_OFF);
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // por complemento ya que un bug en el qr scan pisa el requestCode.
        if (requestCode == AppConstants.LOCATION_SETTINGS_REQUEST_CODE){
            setLocationUpdates(true);
            sharedPreferences.storeValue("location", "true");
            settingsObserver.notifyEvent(AppConstants.SETTINGS_LOCATION_ON);
         //   setupGeofences();
        }
        else{
            passportObserver.onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onBackPressed() {

        class CallbackNo implements CustomCommand{
            @Override
            public void execute() {
            }
        }
        class CallbackYes implements CustomCommand{
            @Override
            public void execute() {
                ActivityCompat.finishAffinity(activity);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }
        MessageDialogHelper.showYesNoDialog(activity,getString(R.string.app_name), getString(R.string.exitMessage),getString(R.string.yes),
                getString(R.string.no),new CallbackYes(), new CallbackNo());


    }
/*
    private void setupGeofences(){
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        List <Geofence>  geofences= GeofenceWrapperService.getInstance(ctx).buildGeofences(brewers);
        registerGeofences(geofences);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private void registerGeofences( List <Geofence>  geofences){
        Log.i("registering","Abut to add geofences");
        if (AndroidCheckHelper.checkLocationPermission(ctx)){
            Log.i("registering","Permisions Checked");

            mGeofencingClient.addGeofences(GeofenceWrapperService.getInstance(ctx).getGeofencingRequest(geofences),
                getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("GEOFENCES", "geofenced successfully added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GEOFENCES", "Error loading geofencing");

                    }
                });
        }
    }

*/
}