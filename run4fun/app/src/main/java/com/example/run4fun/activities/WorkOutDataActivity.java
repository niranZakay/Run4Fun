package com.example.run4fun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.run4fun.Coordinate;
import com.example.run4fun.R;
import com.example.run4fun.WorkOut;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.run4fun.BuildConfig.MAPS_API_KEY;

public class WorkOutDataActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String CALORIES = "calories";
    public static final String AVG_PACE = "avg";
    public static String DATE="date";
    public static String TIME="time";
    public static String DISTANCE="distance";
    public static String COORDINATES="coordinates";

    private String date;
    private String time;
    private String distance;
    private String coordinates;
    private String calories;
    private String avgPace;

    private GoogleMap googleMap;
    private MapView mapView;
    public static String TAG ="WorkOutDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_data);

        Intent intent = this.getIntent();
        date = intent.getExtras().getString(DATE);
        time = intent.getExtras().getString(TIME);
        distance = intent.getExtras().getString(DISTANCE);
        coordinates = intent.getExtras().getString(COORDINATES);
        calories = intent.getExtras().getString(CALORIES);
        avgPace = intent.getExtras().getString(AVG_PACE);
        TextView tvDate = findViewById(R.id.date_value_edit_data_text);
        TextView tvTime = findViewById(R.id.time_value_edit_data_text);
        TextView tvDistance = findViewById(R.id.distatnce_value_edit_data_text);
        TextView tvCalories = findViewById(R.id.calories_value_edit_data_text);
        TextView tvAvgPace = findViewById(R.id.avg_pace_value_edit_data_text);

        //set data to labels
        tvDate.setText(date);
        tvTime.setText(time);
        tvDistance.setText(distance);

        DecimalFormat df= new DecimalFormat("##.##");
        String caloriesFormat=df.format(Double.parseDouble(calories));

        tvCalories.setText(caloriesFormat);
        tvAvgPace.setText(avgPace);

        //map

        Bundle mapViewBundule = null;
        if (savedInstanceState != null) {
            mapViewBundule = savedInstanceState.getBundle(MAPS_API_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapViewWorkOut);
        mapView.onCreate(mapViewBundule);
        mapView.getMapAsync(this);

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
            float zoomLevel = 16.0f;

            for (Coordinate coordinate:coordinatesList)
            {
                latlngs.add(new LatLng(coordinate.latitude, coordinate.longitude));
            }
            PolylineOptions polylineOptions = new PolylineOptions();
            //drew on map
            for (LatLng point : latlngs) {
                polylineOptions.add(point);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoomLevel));
            }
            // Instantiates a new Polyline object and adds points to define a rectangle
            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(polylineOptions);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}