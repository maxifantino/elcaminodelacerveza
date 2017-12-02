package com.mgfdev.elcaminodelacerveza.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import com.mgfdev.elcaminodelacerveza.R;

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
            createNotification(context, craftName, craftUrl);
        }
    }

    private void createNotification(Context context, String craftName, String craftUrl) {
        //hook
        NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(java.text.MessageFormat.format(context.getString(R.string.proximity_advice), craftName, craftUrl))
                .build();//tSma llIcon(R.drawable.abc).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }
}
