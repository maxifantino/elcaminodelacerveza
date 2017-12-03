package com.mgfdev.elcaminodelacerveza.services;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;

import java.util.List;
import java.util.Timer;

import com.mgfdev.elcaminodelacerveza.R;

/**
 * Created by Maxi on 12/11/2017.
 */

public class LocalizationService {

    private static LocalizationService instance;
    private static Context ctx;
    private LocationManager locationManager;
    private CacheManagerHelper cacheManager;
    private String ACTION_PROXIMITY_ALERT = "com.mgfdev.elcaminodelacerveza.ProximityAlert";
    private static TextView log;
    private static FragmentActivity localizationObserver;
    private LocalizationService() {
        cacheManager = CacheManagerHelper.getInstance();
    }

    public static LocalizationService getInstance(FragmentActivity fragmentActivity) {
        if (instance == null) {
            instance = new LocalizationService();
            ctx = fragmentActivity.getApplicationContext();
            localizationObserver = fragmentActivity;
            log = (TextView) fragmentActivity.findViewById(R.id.locationLog);
        }
        return instance;
    }

    public void init() {
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        CacheManagerHelper cacheBrewers = CacheManagerHelper.getInstance();
        populateProximityAlerts(cacheBrewers);
        IntentFilter filter = new IntentFilter(ACTION_PROXIMITY_ALERT);
        localizationObserver.registerReceiver(new LocationUpdateReceiver(), filter);
    }

    public void getLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 300, new CustomLocationListener());

    }

    private void populateProximityAlerts(CacheManagerHelper cacheBrewers) {
        List<BeerLocation> beerLocations = cacheBrewers.getBrewers();
        for (BeerLocation item : beerLocations) {
            Intent intent = new Intent(ACTION_PROXIMITY_ALERT);
            intent.putExtra("brewer", item.getBrewery());
            PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
            try {
                //               locationManager.addProximityAlert(-34.800254, //item.getLatitude(),
                //                     -58.4116293 /*item.getLongitude()*/, GeofencesConstants.GEOFENCE_RADIUS_IN_METERS, -1, pendingIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        if (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Intent intent = new Intent(ACTION_PROXIMITY_ALERT);
        intent.putExtra("brewer", "Lallal");
        PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);

        locationManager.addProximityAlert(-34.790254, //item.getLatitude(),
                -58.4028293 /*item.getLongitude()*/, GeofencesConstants.GEOFENCE_RADIUS_IN_METERS, -1, pendingIntent);
    }

    private class CustomLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            log.setText(Double.toString(location.getLatitude()) + "..." + Double.toString(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            log.setText("Status Changed");
        }

        @Override
        public void onProviderEnabled(String provider) {
            log.setText("On Provider enabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            log.setText("Ese necesario activar el servicio de localización en el menú ajustes/seguridad/localizacion");

        }
    }
}
