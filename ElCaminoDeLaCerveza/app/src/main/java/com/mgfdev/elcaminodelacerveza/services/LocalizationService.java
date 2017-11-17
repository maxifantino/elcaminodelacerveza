package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;

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

    public void checkLocation()
    {
        LatLng latLng = null;
        try {
            locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) {
                try{
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationListenerNetwork);
                }
                catch (SecurityException e) {

                }
            }
            if(isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                            locationListenerGps);
                }
                catch (SecurityException e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            checkBrewersNear(lat, lng);
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
            checkBrewersNear(lat, lng);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void checkBrewersNear(Double lat,Double lng){
        List<BeerLocation> brewers = cacheManager.getBrewers(user.getUsername(), user.getPassword());
        for (BeerLocation brewer: brewers){
            float[] results =new float[3];
            Location.distanceBetween(lat,lng,brewer.getLattitude(),brewer.getLongitude(),results);
            if (results[0] <= 200){
                // hook GSM
            }
        }
    }
}
