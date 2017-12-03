package com.mgfdev.elcaminodelacerveza.services;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class LocationService2 extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }/*
    String proximitysd = "com.apps.ProximityService";
    int n = 0;
    private BroadcastReceiver mybroadcast;
    private LocationManager locationManager;
    MyLocationListener locationListenerp;
    public LocationServivce2() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        mybroadcast = new ProximityIntentReceiver();
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);





        double lat;
        double lng;
        float radius = 50f;
        long expiration = -1;
        MyDBAdapter db = new MyDBAdapter(this);
        Cursor cursor;
        db.read();
        cursor = db.getAllEntries();
        boolean go = cursor.moveToFirst();
        while(cursor.isAfterLast() != true){
            lat = cursor.getInt(MyDBAdapter.LATITUDE_COLUMN)/1E6;
            lng = cursor.getInt(MyDBAdapter.LONGITUDE_COLUMN)/1E6;
            String what = cursor.getString(MyDBAdapter.ICON_COLUMN);
            String how = cursor.getString(MyDBAdapter.FISH_COLUMN);
            String proximitys = "com.apps.ProximityService" + n;
            IntentFilter filter = new IntentFilter(proximitys);
            registerReceiver(mybroadcast, filter );

            Intent intent = new Intent(proximitys);

            intent.putExtra("alert", what);
            intent.putExtra("type", how);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, n, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            locationManager.addProximityAlert(lat, lng, radius, expiration, proximityIntent);
            //sendBroadcast(new Intent(intent));

            n++;
            cursor.moveToNext();
        }
        db.close();
        cursor.close();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Proximity Service Stopped", Toast.LENGTH_LONG).show();
        try{
            unregisterReceiver(mybroadcast);
        }catch(IllegalArgumentException e){
            Log.d("reciever",e.toString());
        }


    }
    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Proximity Service Started", Toast.LENGTH_LONG).show();
        //IntentFilter filter = new IntentFilter(proximitys);
        //registerReceiver(mybroadcast,filter);


    }
    public class ProximityIntentReceiver extends BroadcastReceiver{
        private static final int NOTIFICATION_ID = 1000;
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String key = LocationManager.KEY_PROXIMITY_ENTERING;

            Boolean entering = arg1.getBooleanExtra(key, false);
            String here = arg1.getExtras().getString("alert");
            String happy = arg1.getExtras().getString("type");



            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, arg1, 0);

            Notification notification = createNotification();

            notification.setLatestEventInfo(arg0,
                    "Entering Proximity!", "You are approaching a " + here + " marker.", pendingIntent);

            notificationManager.notify(NOTIFICATION_ID, notification);


        }

        private Notification createNotification() {
            Notification notification = new Notification();

            notification.icon = R.drawable.icon;
            notification.when = System.currentTimeMillis();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            notification.ledARGB = Color.WHITE;
            notification.ledOnMS = 1500;
            notification.ledOffMS = 1500;


            return notification;
        }
        //make actions



    }
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Toast.makeText(getApplicationContext(), "I was here", Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
        }
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }
    }
*/
}