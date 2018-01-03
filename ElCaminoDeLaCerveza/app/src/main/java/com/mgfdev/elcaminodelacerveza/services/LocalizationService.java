package com.mgfdev.elcaminodelacerveza.services;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import static com.mgfdev.elcaminodelacerveza.helpers.NotificationHelper.createNotification;

/**
 * Created by Maxi on 12/11/2017.
 */

public class LocalizationService {

    private static LocalizationService instance;
    private static Context ctx;
    private LocationManager locationManager;
    private CacheManagerHelper cacheManager;
    private String ACTION_PROXIMITY_ALERT = "com.mgfdev.elcaminodelacerveza.services.ProximityAlert";
    private static FragmentActivity localizationObserver;
    private LocalizationService() {
        cacheManager = CacheManagerHelper.getInstance();
    }
    private static CustomLocationListener locationListener;

    public static LocalizationService getInstance(FragmentActivity fragmentActivity) {
        if (instance == null) {
            instance = new LocalizationService();
            ctx = fragmentActivity.getApplicationContext();
            localizationObserver = fragmentActivity;
       //     locationListener = new LocationListener();
        }
        return instance;
    }

    // Al menos en android 7, a veces devuelve true aun cuando el gps está apagado, causando mala experiencia usuario.
    public boolean isLocationActive2() {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        return gps_enabled || network_enabled;
    }
    // workaround bug android

    public boolean isLocationActive() {
     return getLastKnownLocation() != null;
    }

    public void init() {
        if (locationManager == null){
            locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        }

      //  locationListener = new CustomLocationListener();
    //    CacheManagerHelper cacheBrewers = CacheManagerHelper.getInstance();
     //   getLocationUpdates();
      //  populateProximityAlerts(cacheBrewers);
       // IntentFilter filter = new IntentFilter(ACTION_PROXIMITY_ALERT);
       // localizationObserver.registerReceiver(new LocationUpdateReceiver(), filter);

    }

    public Location getLastKnownLocation() {
        Location result = null;

        boolean hasAccess = (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED);
        result = hasAccess ?  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :  null;
        if (result == null){
            result =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        /*if (result == null){
            result = getCurrentLocation();
        }*/
        return result ;
    }

    public Location getCurrentLocation() {
        long MIN_TIME_BW_UPDATES = 10000;
        float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000;
        boolean isGPSEnabled, isNetworkEnabled;
        Location result = null;
        try {

            locationManager = (LocationManager) ctx
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled) {
                // if GPS Enabled get lat/long using GPS Services
                result = getLocation(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);
            }
            if (result == null && isNetworkEnabled) {
                // if GPS Enabled get lat/long using GPS Services
                result = getLocation(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);
            }

        } catch (Exception e) {
            Log.e("getCurrentLocation", "Error while getting location" + e.getMessage());
        }
        return result;
    }

    private Location getLocation (String provider, Long mins, float meters){
        Location result = null;
        boolean hasAccess = (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED);
   /*     if (hasAccess) {
            locationManager.requestLocationUpdates(
                    provider,
                    mins,
                    meters, locationListener);

            if (locationManager != null) {
                result = locationManager
                        .getLastKnownLocation(provider);
            }
        }*/
        return result;
    }

    public boolean nearby (Location currentLocation, Location destLocation, Integer tolerance){
        Float distance = currentLocation.distanceTo(destLocation);
        return (distance.intValue() < tolerance);
    }

    public void stop(){
//        locationManager.removeUpdates(locationListener);
    }
 /**0   public void getLocationUpdates() {
        Long meters = Long.parseLong(SharedPreferenceManager.getInstance(ctx).getStringValue("meters"));
        Long mins = Long.parseLong(SharedPreferenceManager.getInstance(ctx).getStringValue("time"));
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (meters < 200l){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mins, meters, locationListener);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mins, meters, locationListener);
        }
    }
*/
    public Location locationFactory (Double latitude, Double longitude ){
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


    private void populateProximityAlerts(CacheManagerHelper cacheBrewers) {
        List<BrewerInfo> brewerInfos = cacheBrewers.getBrewers();
        int meters = Integer.valueOf(StringUtils.defaultString(SharedPreferenceManager.getInstance(ctx).getStringValue("meters"), Integer.toString(GeofencesConstants.GEOFENCE_RADIUS_IN_METERS)));
        for (BrewerInfo item : brewerInfos) {
            Intent intent = new Intent(ACTION_PROXIMITY_ALERT);
            intent.putExtra("brewer", item.getBrewery());
            intent.putExtra("address", item.getAddress());
            intent.putExtra("brewerUrl",buildGMapsIntentExtra(item.getLatitude(),item.getLongitude()));

            PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
            try {
                    locationManager.addProximityAlert(item.getLatitude(),
                            item.getLongitude(), meters, -1, pendingIntent);
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
        //printDumy
        BrewerInfo item = brewerInfos.get(0);
        createNotification(ctx, item.getBrewery(),buildGMapsIntentExtra(item.getLatitude(), item.getLongitude()), item.getAddress());

         //end printDumy
        locationManager.addProximityAlert(-34.790254, //item.getLatitude(),
                -58.4028293 /*item.getLongitude()*/, GeofencesConstants.GEOFENCE_RADIUS_IN_METERS, -1, pendingIntent);
    }

    private String buildGMapsIntentExtra(Double latitude, Double longitude){
        String extra = "geo:" + Double.toString(latitude) + "," + Double.toString(longitude);
        return extra;
    }

    private class CustomLocationListener implements com.google.android.gms.location.LocationListener{

        @Override
        public void onLocationChanged(Location location) {

        }

    }
}
