package com.mgfdev.elcaminodelacerveza.services;

import com.google.android.gms.location.Geofence;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxi on 18/11/2017.
 */

public class GeofenceWrapperService {

    private static GeofenceWrapperService instance;
    private List<Geofence> geofences;
    private GeofenceWrapperService(){}
    private String username;
    private String password;

    public static GeofenceWrapperService getInstance(String username, String password){
        if (instance == null){
            instance = new GeofenceWrapperService();
            instance.username = username;
            instance.password = password;
        }
        return instance;
    }

    public void init(){
        geofences = new ArrayList<Geofence>();
        CacheManagerHelper cacheBrewers =  CacheManagerHelper.getInstance();
        List<BeerLocation>beerLocations = cacheBrewers.getBrewers(username, password);
        for (BeerLocation beerlocation: beerLocations){
            geofences.add(new Geofence.Builder()
                    .setRequestId(beerlocation.getCraftName())
                    .setCircularRegion(beerlocation.getLattitude(), beerlocation.getLongitude(), GeofencesConstants.GEOFENCE_RADIUS_IN_METERS)
                    .setExpirationDuration(GeofencesConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }
    }


}
