package com.example.run4fun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.run4fun.Coordinate;
import com.example.run4fun.R;
import com.example.run4fun.db.DataAccess;
import com.example.run4fun.db.WorkOutSchema;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.run4fun.BuildConfig.MAPS_API_KEY;

public class WorkOutActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener{
    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;

    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;

    //for WorkOut instance
    private static String date;
    private static String distance="0";
    public static String time;
    private static List<Coordinate> coordinates =new ArrayList<>();

    private static String TAG = "WorkOutActivity:";
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out);

        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        runTimer();
        //show pause button
        final Button pauseButton = findViewById(R.id.pause_button);
        pauseButton.setVisibility(View.VISIBLE);

        //hide resume button
        final Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setVisibility(View.GONE);

        Bundle mapViewBundule = null;
        if (savedInstanceState != null) {
            mapViewBundule = savedInstanceState.getBundle(MAPS_API_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapViewWorkOut);
        mapView.onCreate(mapViewBundule);
        mapView.getMapAsync(this);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        //get current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        date = sdf.format(new Date());
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //set on map
        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Me"));
        coordinates.add(new Coordinate(location.getLatitude(),location.getAltitude()));
    }


    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
        mapView.onPause();
    }


    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickPause(View view) {
        //show resume button
        final Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setVisibility(View.VISIBLE);

        //hide pause button
        final Button pauseButton = findViewById(R.id.pause_button);
        pauseButton.setVisibility(View.GONE);

        running = false;
    }

    public void onClickResume(View view) {
        //show pause button
        final Button pauseButton = findViewById(R.id.pause_button);
        pauseButton.setVisibility(View.VISIBLE);

        //hide resume button
        final Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setVisibility(View.GONE);

        running = true;
    }

    public void onClickFinish(View view) {
        createAlertBeforeFinish();
    }

    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer() {
        running = true;
        // Get the text view.
        final TextView timeView
                = (TextView) findViewById(
                R.id.timer_textview);

        // Creates a new Handler
        final Handler handler
                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run() {

                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, secs);

                // Set the text view text.
                timeView.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void createAlertBeforeFinish() {

        String jsonCoordinates =  new Gson().toJson(coordinates);
        TextView timerTextView = (TextView)findViewById(R.id.timer_textview);
        time = timerTextView.getText().toString();

        //move date,time,distance,coordinates to WorkOutFinishActivity
        Intent intent = new Intent(getBaseContext(), WorkOutFinishActivity.class);
        intent.putExtra(WorkOutFinishActivity.DATE,date);
        intent.putExtra(WorkOutFinishActivity.TIME,time);
        intent.putExtra(WorkOutFinishActivity.DISTANCE,distance);
        intent.putExtra(WorkOutFinishActivity.COORDINATES,jsonCoordinates);
        startActivity(intent);
        finish();

        //stop get location
        locationManager.removeUpdates(this);
        locationManager = null;


    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}
