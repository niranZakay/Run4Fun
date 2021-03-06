package com.example.run4fun.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.run4fun.Coordinate;
import com.example.run4fun.R;
import com.example.run4fun.services.LocationService;
import com.example.run4fun.util.Pref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.run4fun.services.LocationService.DISABLE_GPS;
import static com.example.run4fun.services.LocationService.LOCATION;

public class WorkOutActivity extends AppCompatActivity  {
    public static final String CHANNEL_ID = "run4fun";
    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;


    public static double KM_IN_METERS = 1000;
    public static double SEC_IN_MINUTE = 60;
    public static double RUN_CONST = 0.16;


    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;

    //for WorkOut instance
    private static String date;
    private static double distance = 0;
    public static String time;
    private static String avgPace = "00:00";
    private static double calories = 0;

    boolean pasueIsOn = false;

    private static List<Coordinate> coordinates = new ArrayList<>();
    private TextView tvDistance;
    private TextView tvAvgPace;
    private TextView tvCalories;
    private TextView tvTimer;

    private static String TAG = "WorkOutActivity:";
    private LocationManager locationManager;
    public BroadcastReceiver broadcastReceiver;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startService();
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
        tvCalories = findViewById(R.id.cal_value_textview);
        tvDistance = findViewById(R.id.distance_value_textview);
        tvTimer = findViewById(R.id.timer_textview);


        //get current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        date = sdf.format(new Date());

        //register broadcastreciver for location

         broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                Location location = (Location)intent.getExtras().get(LOCATION);
                if(location!=null)
                {
                    onLocationChanged(location.getLatitude(),location.getLongitude());
                }


//                String string = (String)intent.getExtras().get(DISABLE_GPS);
//                if(string!=null)
//                {
//                 onProviderDisabled();
//                }


                }


        };




    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startService()
    {
        createNotificationChannel();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
        Intent serviceIntent = new Intent(this, LocationService.class);
        startForegroundService(serviceIntent);
    }

    public void stopService()
    {
        Intent serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
        unregisterReceiver(broadcastReceiver);
    }



    public void onLocationChanged(double latitude,double longitude) {


        if (running) {
            Log.i(TAG, "Latitude:" + latitude + ", Longitude:" +  longitude);
            coordinates.add(new Coordinate(latitude, longitude));
            if (coordinates.size() > 1) {
                //calculate distance by the last point and the new point
                distance += CalculationByDistance(new LatLng(coordinates.get(coordinates.size() - 2).latitude, coordinates.get(coordinates.size() - 2).longitude), new LatLng(latitude, longitude));
                //display it
                DecimalFormat df = new DecimalFormat("#.###");
                String dx = df.format(distance / KM_IN_METERS);
                tvDistance.setText(dx + " KM");

                String currentTimeByView = tvTimer.getText().toString();
                String[] arrayTimeer = currentTimeByView.split(":");
                double minutes = Double.parseDouble(arrayTimeer[0]);
                double seconds = Double.parseDouble(arrayTimeer[1]);
                double totalSeconds = minutes * SEC_IN_MINUTE + seconds;


                //calculate Avg Pace
                double avgPace = (totalSeconds / SEC_IN_MINUTE) / (distance / KM_IN_METERS);
                int min = (int) (avgPace % 60);
                int sec = (int) ((avgPace / 60) % 60);
                tvAvgPace.setText(String.format("%02d:%02d", min, sec));
                this.avgPace = String.format("%02d:%02d", min, sec);

                //calculate calories by weight minutes and const
                double weight = Double.parseDouble(Pref.getValue(this, "weight", "80"));
                calories = RUN_CONST * weight * (totalSeconds / SEC_IN_MINUTE);
                DecimalFormat dc = new DecimalFormat("#");
                String formatCalories = dc.format(calories);
                tvCalories.setText(String.valueOf(formatCalories));

            }

        }

    }


    public void onProviderDisabled() {

        // notify user
        new AlertDialog.Builder(getBaseContext())
                .setMessage(R.string.gps_network_not_enabled)
                .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel_text, null)
                .show();
        running = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,LocationService.class);
        startService(intent);
        running = true;

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
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
    }


    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
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

                int minutes = (int) ((seconds % 3600) / SEC_IN_MINUTE);
                int secs = (int) (seconds % SEC_IN_MINUTE);

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


    public void createAlertBeforStop() {
        //make vibration
        running = false;
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkOutActivity.this);

        builder.setTitle(getString(R.string.alert_text));
        builder.setMessage(getString(R.string.alert_before_stop_text));

        builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String jsonCoordinates = new Gson().toJson(coordinates);
                TextView timerTextView = (TextView) findViewById(R.id.timer_textview);
                time = timerTextView.getText().toString();

                //move date,time,distance,coordinates to WorkOutFinishActivity
                Intent intent = new Intent(getBaseContext(), WorkOutFinishActivity.class);
                intent.putExtra(WorkOutFinishActivity.DATE, date);
                intent.putExtra(WorkOutFinishActivity.TIME, time);
                intent.putExtra(WorkOutFinishActivity.CALORIES, String.valueOf(calories));
                intent.putExtra(WorkOutFinishActivity.AVG_PACE, avgPace);

                //convert distance from double to string format
                DecimalFormat df = new DecimalFormat("##.##");
                String distanceFormat = df.format(distance/ KM_IN_METERS);
                intent.putExtra(WorkOutFinishActivity.DISTANCE, distanceFormat);

                intent.putExtra(WorkOutFinishActivity.COORDINATES, jsonCoordinates);
                startActivity(intent);
                stopService();

                jsonCoordinates = "";
                distance = 0;
                avgPace= "00:00";
                calories = 0;

                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no_text), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
                running = true;
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        running = false;
        //make vibration
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkOutActivity.this);

        builder.setTitle(getString(R.string.alert_text));
        builder.setMessage(getString(R.string.alert_before_discard_text));

        builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //back to main activity
                finish();
                stopService();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no_text), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                running = true;
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
