package com.mgfdev.elcaminodelacerveza.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Maxi on 08/12/2017.
 */

public class ConnectionHelper {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }
}
