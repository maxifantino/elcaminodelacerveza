package com.mgfdev.elcaminodelacerveza.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.mgfdev.elcaminodelacerveza.R;

/**
 * Created by Maxi on 23/12/2017.
 */

public class AndroidCheckHelper {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static boolean checkLocationPermission (Context ctx){
        boolean hasPermission= ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        return hasPermission;
    }

    public static void requestLocationPermission (Activity act){
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
        }
}
