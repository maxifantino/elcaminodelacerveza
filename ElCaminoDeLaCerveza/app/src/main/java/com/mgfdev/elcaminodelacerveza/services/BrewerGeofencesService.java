package com.mgfdev.elcaminodelacerveza.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.AndroidCheckHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by azcs on 04/03/17.
 */

public class BrewerGeofencesService {

    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencePendingIntent ;
    private Activity ctx;
    private List<BrewerInfo> brewers;
    private static BrewerGeofencesService instance;
    private GeofencingClient mGeofencingClient;

    public static BrewerGeofencesService getInstance(Activity ctx,  List<BrewerInfo> brewers){
        if (instance == null){
            instance = new BrewerGeofencesService(ctx,brewers);
        }

        return instance;
    }

    private BrewerGeofencesService(Activity ctx,  List<BrewerInfo> brewers){
        this.ctx = ctx;
        this.brewers= brewers;
        if (mGeofencingClient == null){
            mGeofencingClient = LocationServices.getGeofencingClient(ctx);
        }
    }

    public void startGeofencing(){
        if (AndroidCheckHelper.checkLocationPermission(ctx)) {

            mGeofencingClient.addGeofences(
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).addOnSuccessListener (new GeofenceSuccessListener()) // Result processed in onResult().
                    .addOnFailureListener(new GeofenceFailureListener())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("BrewerLocationService","Geofences loading process completed ");
                        }
                    });

            Log.i("BrewerLocationService","Geofences added to locationService ");

        }
        else{
            Log.e("BrewerLocationService","Location Permissions not available. ");
        }
    }

    private class GeofenceSuccessListener implements OnSuccessListener<Void>{
        @Override
        public void onSuccess (Void aVoid){
            Log.i(getClass().getSimpleName(), "Geofences Successfully connected and added");
        }
    }

    private class GeofenceFailureListener implements OnFailureListener{

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.i(getClass().getSimpleName(), "Geofences connection failed. No geofences added");

        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(buildGeofenceList());
        return builder.build();
    }

    private List<Geofence>  buildGeofenceList(){
        return GeofenceWrapperService.getInstance(ctx).buildGeofences(brewers);
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(ctx, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(ctx, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    public void stopGeofencing(){
        mGeofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(ctx, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      Log.i("stopGeofencing", "Geofences removed");
                    }
                })
                .addOnFailureListener(ctx, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("stopGeofencing", "ERROR while trying to remove geofences");                    }
                });
    }


}