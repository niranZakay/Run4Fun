package com.example.run4fun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.run4fun.Coordinate;
import com.example.run4fun.R;
import com.example.run4fun.WorkOut;
import com.example.run4fun.db.DataAccess;
import com.example.run4fun.db.WorkOutSchema;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.run4fun.BuildConfig.MAPS_API_KEY;

public class WorkOutFinishActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static WorkOut workOut = new WorkOut();
    public static String DATE="date";
    public static String TIME="time";
    public static String DISTANCE="distance";
    public static String COORDINATES="coordinates";
    public static String TAG ="WorkOutFinishActivity";
    private String date;
    private String time;
    private String distance;
    private String coordinates;
    private GoogleMap googleMap;
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_finish);
        Intent intent = this.getIntent();
        date = intent.getExtras().getString(DATE);
        time = intent.getExtras().getString(TIME);
        distance = intent.getExtras().getString(DISTANCE);
        coordinates = intent.getExtras().getString(COORDINATES);
        TextView tvDate = findViewById(R.id.date_value_edit_finish_text);
        TextView tvTime = findViewById(R.id.time_value_edit_finish_text);
        TextView tvDistance = findViewById(R.id.distatnce_value_edit_finish_text);

        //set data to labels
        tvDate.setText(date);
        tvTime.setText(time);
        tvDistance.setText(distance);

        Button saveButton = findViewById(R.id.save_button);
        Button discardButton = findViewById(R.id.discard_button);

        Bundle mapViewBundule = null;
        if (savedInstanceState != null) {
            mapViewBundule = savedInstanceState.getBundle(MAPS_API_KEY);
        }
       mapView = (MapView) findViewById(R.id.mapViewWorkOut);
        mapView.onCreate(mapViewBundule);
        mapView.getMapAsync(this);


                //saved button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(300);
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkOutFinishActivity.this);

                builder.setTitle(getString(R.string.alert_text));
                builder.setMessage(getString(R.string.alert_before_finish_text));

                builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //save the workout in db sqlite
                        DataAccess dataAccess = DataAccess.DataAccess(getApplicationContext(), WorkOutSchema.databaseName);
                        boolean result = dataAccess.addWorkOut(date, distance, time, coordinates);
                        if (result) {
                            //save success
                            Log.i(TAG, "db save results: successfully");
                            Toast.makeText(getApplicationContext(), getString(R.string.save_db_success_text), Toast.LENGTH_SHORT).show();

                        } else {
                            //save failed
                            Log.i(TAG, "db save results: failed");
                            Toast.makeText(getApplicationContext(), getString(R.string.save_db_failed_text), Toast.LENGTH_SHORT).show();
                        }


                        //back to main activity
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

        });

        //discard button
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(300);
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkOutFinishActivity.this);

                builder.setTitle(getString(R.string.alert_text));
                builder.setMessage(getString(R.string.alert_before_discard_text));

                builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //back to main activity
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
        });
    }

    public Coordinate[] fromGsonToList()
    {
        Gson gson = new Gson();
        Coordinate[] coordinatesList =gson.fromJson(coordinates, Coordinate[].class);
       return coordinatesList;

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

        Coordinate[] coordinatesList = fromGsonToList();
        if(coordinatesList!=null)
        {
            MarkerOptions options = new MarkerOptions();
            ArrayList<LatLng> latlngs = new ArrayList<>();

            for (Coordinate coordinate:coordinatesList)
            {
                latlngs.add(new LatLng(coordinate.latitude, coordinate.longitude));
            }

            //drew on map
            for (LatLng point : latlngs) {
                options.position(point);
                options.title("Me");
                options.snippet("someDesc");
                googleMap.addMarker(options);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}