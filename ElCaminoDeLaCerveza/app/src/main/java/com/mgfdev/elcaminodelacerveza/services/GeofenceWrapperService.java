package com.mgfdev.elcaminodelacerveza.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxi on 18/11/2017.
 */

public class GeofenceWrapperService {

    private static GeofenceWrapperService instance;
    private static Context ctx;
    private GeofenceWrapperService(){}

    public static GeofenceWrapperService getInstance(Context ctx){
        if (instance == null){
            instance = new GeofenceWrapperService( ctx);
        }
        return instance;
    }

    private GeofenceWrapperService(Context ctx){
        this.ctx = ctx;
    }
    public  List <Geofence> buildGeofences(List<BrewerInfo> brewers){
        List <Geofence> geofences = new ArrayList<Geofence>();
        Long meters = Long.parseLong( SharedPreferenceManager.getInstance(ctx).getStringValue("meters"));
        Long miliseconds =   Long.parseLong(SharedPreferenceManager.getInstance(ctx).getStringValue("time")) * 1000 *60 *60;

        for (BrewerInfo beerlocation: brewers){
            geofences.add(new Geofence.Builder()
                    .setRequestId(beerlocation.getBrewery())
                    .setCircularRegion(beerlocation.getLatitude(), beerlocation.getLongitude(),meters)
                    .setExpirationDuration(miliseconds)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }
        return geofences;
    }

    public GeofencingRequest getGeofencingRequest( List <Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

  /*  public PendingIntent getGeofencePendingIntent() {
        return  new Intent();
    }
*/

}
