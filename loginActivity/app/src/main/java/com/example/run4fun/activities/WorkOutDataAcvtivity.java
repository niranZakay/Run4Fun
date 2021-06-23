package com.example.run4fun.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.run4fun.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WorkOutDataAcvtivity extends AppCompatActivity {

    public static String DATE="date";
    public static String TIME="time";
    public static String DISTANCE="distance";
    public static String COORDINATES="coordinates";
    public static String CALORIES="Calories";
    public static String AVERAGE_VELOCITY ="AverageVelocity";

    private String date;
    private String time;
    private String distance;
    private String coordinates;
    private String calories;
    private String averageVelocity;

    private GoogleMap googleMap;
    private MapView mapView;
    public static String TAG ="WorkOutFinishActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_data_component);

        Intent intent = this.getIntent();
        date = intent.getExtras().getString(DATE);
        time = intent.getExtras().getString(TIME);
        distance = intent.getExtras().getString(DISTANCE);
        coordinates = intent.getExtras().getString(COORDINATES);
        TextView tvDate = findViewById(R.id.date_value_edit_data_text);
        TextView tvTime = findViewById(R.id.time_value_edit_data_text);
        TextView tvDistance = findViewById(R.id.distance_value_textview);

        //set data to labels
        tvDate.setText(date);
        tvTime.setText(time);
        tvDistance.setText(distance);

    }
}