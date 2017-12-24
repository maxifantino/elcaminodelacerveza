package com.mgfdev.elcaminodelacerveza.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.mgfdev.elcaminodelacerveza.R;

/**
 * Created by Maxi on 11/12/2017.
 */

public class NotificationHelper {

    public static void createNotification(Context context, String craftName, String craftUrl, String address) {
        String message = java.text.MessageFormat.format(context.getString(R.string.proximity_advice), craftName, address);
        NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mapsIntent = buildGmapIntent(context, craftUrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mapsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder builder = new  android.support.v7.app.NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_transparent_med)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setStyle(new  android.support.v7.app.NotificationCompat.BigTextStyle().bigText(message))
                .setTicker(context.getString(R.string.app_name))
                .setDefaults(NotificationCompat.DEFAULT_ALL);


        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notify=builder
                .build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }
    private static Intent buildGmapIntent (Context ctx, String url){
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;
    }
}
