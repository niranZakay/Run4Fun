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
import com.example.run4fun.util.Pref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.run4fun.BuildConfig.MAPS_API_KEY;

public class WorkOutActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener{
    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;

    public static int KM_IN_METERS =1000;
    public static int SEC_IN_MINUTE =60;
    public static double RUN_CONST =0.16;
    public static double WALK_CONST =0.007;

    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;

    //for WorkOut instance
    private static String date;
    private static double distance=0;
    public static String time;
    private static double velocity=0;
    private static String avgPace="00:00";
    private static double calories=0;

    boolean pasueIsOn = false;

    private static List<Coordinate> coordinates =new ArrayList<>();
    private TextView tvDistance;
    private TextView tvAvgPace;
    private TextView tvCalories;
    private TextView tvVelocity;
    private TextView tvTimer;

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

        tvAvgPace = findViewById(R.id.pace_value_textview);
        tvCalories =findViewById(R.id.cal_value_textview);
        tvDistance = findViewById(R.id.distance_value_textview);
        tvVelocity = findViewById(R.id.velocity_value_textview);
        tvTimer = findViewById(R.id.timer_textview);

        Bundle mapViewBundule = null;
        if (savedInstanceState != null) {
            mapViewBundule = savedInstanceState.getBundle(getString(R.string.google_map_key));
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

        if(running) {
            Log.i(TAG, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
//        //set on map
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Me"));

            // Instantiates a new Polyline object and adds points to define a rectangle
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(new LatLng(location.getLatitude(), location.getLongitude()));//
            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(polylineOptions);
            //zoom it
            float zoomLevel = 16.0f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));
            coordinates.add(new Coordinate(location.getLatitude(), location.getLongitude()));
            if (coordinates.size() > 1) {
                //calculate distance by the last point and the new point
                distance += CalculationByDistance(new LatLng(coordinates.get(coordinates.size() - 2).latitude, coordinates.get(coordinates.size() - 2).longitude), new LatLng(location.getLatitude(), location.getLongitude())) / KM_IN_METERS;
                //display it
                DecimalFormat df = new DecimalFormat("#.##");
                String dx = df.format(distance);
                tvDistance.setText(dx + " KM");

                //calculate distance by distance and time (KM/h)
                String currentTimeByView = tvTimer.getText().toString();
                String[] arrayTimeer = currentTimeByView.split(":");
                double minutes = Double.parseDouble(arrayTimeer[0]);
                double seconds = Double.parseDouble(arrayTimeer[1]);
                double totalSeconds = minutes * SEC_IN_MINUTE + seconds;
                velocity = distance / totalSeconds;
                if (velocity > 0) {
                    //display it
                    DecimalFormat dv = new DecimalFormat("#");
                    String formatVelocity = df.format(velocity);
                    tvVelocity.setText(formatVelocity + " KM/h");
                }


                //calculate Avg Pace by velocity
                double avgPace = KM_IN_METERS / velocity;
                int sec = (int) (avgPace % 60);
                int min = (int) ((avgPace / 60) % 60);
                tvAvgPace.setText(String.format("%02d:%02d", min, sec));
                this.avgPace = String.format("%02d:%02d", min, sec);

                //calculate calories by weight minutes and const
                double weight = Double.parseDouble(Pref.getValue(this, "weight", "80"));
                calories = RUN_CONST * weight * (totalSeconds / 60);
                DecimalFormat dc = new DecimalFormat("#");
                String formatCalories = dc.format(calories);
                tvCalories.setText(String.valueOf(formatCalories));


            }
        }

    }

    public double CalculationByDistance(LatLng startP, LatLng endP) {
        return SphericalUtil.computeDistanceBetween(startP, endP);

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
        pasueIsOn = false;
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

    public void onClickStop(View view) {
        createAlertBeforStop();
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

                    int minutes = (seconds % 3600) / SEC_IN_MINUTE;
                    int secs = seconds % SEC_IN_MINUTE;

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


    public void createAlertBeforStop()
    {
        //make vibration
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkOutActivity.this);

        builder.setTitle(getString(R.string.alert_text));
        builder.setMessage(getString(R.string.alert_before_stop_text));

        builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String jsonCoordinates =  new Gson().toJson(coordinates);
                TextView timerTextView = (TextView)findViewById(R.id.timer_textview);
                time = timerTextView.getText().toString();

                //move date,time,distance,coordinates to WorkOutFinishActivity
                Intent intent = new Intent(getBaseContext(), WorkOutFinishActivity.class);
                intent.putExtra(WorkOutFinishActivity.DATE,date);
                intent.putExtra(WorkOutFinishActivity.TIME,time);
                intent.putExtra(WorkOutFinishActivity.CALORIES,String.valueOf(calories));
                intent.putExtra(WorkOutFinishActivity.AVG_PACE,avgPace);

                //convert distance from double to string format
                DecimalFormat df = new DecimalFormat("##.##");
                String distanceFormat=df.format(distance);
                intent.putExtra(WorkOutFinishActivity.DISTANCE,distanceFormat);

                intent.putExtra(WorkOutFinishActivity.COORDINATES,jsonCoordinates);
                startActivity(intent);
                finish();

                //stop get location
                locationManager.removeUpdates(WorkOutActivity.this);
                locationManager = null;

                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no_text), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
