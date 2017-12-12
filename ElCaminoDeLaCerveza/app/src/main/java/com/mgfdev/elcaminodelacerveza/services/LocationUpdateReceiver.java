package com.mgfdev.elcaminodelacerveza.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.helpers.NotificationHelper;

/**
 * Created by Maxi on 18/11/2017.
 */

public class LocationUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final String key = LocationManager.KEY_PROXIMITY_ENTERING;
        final Boolean sendNotification = intent.getBooleanExtra(key, false);
        String craftName = intent.getStringExtra("brewer");
        String craftUrl = intent.getStringExtra("brewerUrl");
        String craftAddress = intent.getStringExtra("address");
        if (sendNotification) {
            NotificationHelper.createNotification(context, craftName, craftUrl,craftAddress);
        }
    }


}
