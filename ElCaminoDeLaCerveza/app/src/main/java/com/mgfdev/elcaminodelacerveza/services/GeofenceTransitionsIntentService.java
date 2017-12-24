package com.mgfdev.elcaminodelacerveza.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.helpers.NotificationHelper;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String intentName = "GEOFENCE_INTENT";
    private Context ctx;

    public GeofenceTransitionsIntentService(){
        super(intentName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {

            Log.e("ERROR", "Error handling geofencing " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String id = triggeringGeofences.get(0).getRequestId();
            // get fields by id
            BrewerInfo brewer = CacheManagerHelper.getInstance().getBrewerById(id);
            String craftName = brewer != null ? brewer.getBrewery() : "";
            String craftUrl = brewer != null ? buildGMapsIntentExtra(brewer.getLatitude(), brewer.getLongitude()): "";
            String craftAddress = brewer != null ? brewer.getAddress() : "";

            // Send notification and log the transition details.
            NotificationHelper.createNotification(getApplicationContext(), craftName, craftUrl,craftAddress);
        } else {
            // Log the error.
            Log.e("ERROR", "Geofence transition unknown: " + geofenceTransition);
        }
    }

    private String buildGMapsIntentExtra(Double latitude, Double longitude){
        String extra = "geo:" + Double.toString(latitude) + "," + Double.toString(longitude);
        return extra;
    }

}