package com.mgfdev.elcaminodelacerveza.helpers;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Maxi on 25/11/2017.
 */

public class CheckHelper {

    public static boolean checkLocalization(Context ctx)
    {
        String fineLocationPermission = "android.permission.Manifest.permission.ACCESS_FINE_LOCATION";
        String coarseLocationPermission = "android.permission.Manifest.permission.ACCESS_COARSE_LOCATION";
        int res = ctx.checkCallingOrSelfPermission(fineLocationPermission) + ctx.checkCallingOrSelfPermission(coarseLocationPermission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
