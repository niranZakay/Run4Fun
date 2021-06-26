package com.example.run4fun.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class LocationService extends Service implements LocationListener {
    private TextView tvDistance;
    private TextView tvAvgPace;
    private TextView tvCalories;
    private TextView tvTimer;
    private LocationManager locationManager;


    public LocationService(TextView tvDistance,TextView tvAvgPace,TextView tvCalories, TextView tvTimer)
    {
        this.tvDistance = tvDistance;
        this.tvAvgPace = tvAvgPace;
        this.tvCalories = tvCalories;
        this.tvTimer = tvTimer;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        

        return START_NOT_STICKY;

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}