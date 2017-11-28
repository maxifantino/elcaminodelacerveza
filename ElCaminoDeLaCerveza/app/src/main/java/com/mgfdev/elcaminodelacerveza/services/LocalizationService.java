package com.mgfdev.elcaminodelacerveza.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;

import java.util.List;
import java.util.Timer;

/**
 * Created by Maxi on 12/11/2017.
 */

public class LocalizationService {

    private static LocalizationService instance ;
    private static Context ctx;
    private static final int MIN_TIME_BW_UPDATES = 5;
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES =  100;
    private LocationManager locationManager;
    private Timer timer;
    private CacheManagerHelper cacheManager;
    private User user;
    private String ACTION_PROXIMITY_ALERT = "com.mgfdev.elcaminodelacerveza.ProximityAlert";

    private LocalizationService (){
        cacheManager = CacheManagerHelper.getInstance();
    }

    public static LocalizationService getInstance (Context context, User user){
        if (instance == null){
            instance = new LocalizationService();
            ctx = context;
            instance.user = user;
        }
        return instance;
    }

    public void init()
    {
        LatLng latLng = null;
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        CacheManagerHelper cacheBrewers =  CacheManagerHelper.getInstance();
        List<BeerLocation>beerLocations = cacheBrewers.getBrewers(user.getUsername(), user.getPassword());
        for (BeerLocation item:beerLocations) {
            Intent intent = new Intent(ACTION_PROXIMITY_ALERT);
            intent.putExtra("brewer", item.getBrewery());
            PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
            try {
                locationManager.addProximityAlert(item.getLatitude(),
                        item.getLongitude(), GeofencesConstants.GEOFENCE_RADIUS_IN_METERS, -1, pendingIntent);
            }   catch (SecurityException e) {
                    e.printStackTrace();
            }
        }
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            Double lat =location.getLatitude();
            Double lng = location.getLongitude();
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
            // HOOK !!!!!
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            Double lat =location.getLatitude();
            Double lng = location.getLongitude();
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);
            //HOOK
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

}