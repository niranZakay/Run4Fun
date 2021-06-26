package com.example.run4fun.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.run4fun.R;
import com.example.run4fun.activities.WorkOutActivity;

import static com.example.run4fun.activities.WorkOutActivity.CHANNEL_ID;

public class LocationService extends Service implements LocationListener {

    public static String LOCATION="location.locationListener";
    public static String DISABLE_GPS ="disablegps.locationListener";
    public static String BROADCAST_ACTION ="locationListener.broadcast";
    LocationManager locationManager;

public LocationService()
{

}


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, WorkOutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("in process...")
                .setSmallIcon(R.drawable.ic_runner_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return START_NOT_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        return START_NOT_STICKY;

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(LOCATION,location);
        sendBroadcast(intent);

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(DISABLE_GPS,new String("true"));
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        endGPS();
    }

    public void endGPS() {
        try {
            locationManager.removeUpdates(this);
            locationManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}