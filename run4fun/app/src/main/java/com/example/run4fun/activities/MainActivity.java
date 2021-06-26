package com.example.run4fun.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.example.run4fun.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //click to move StatisticalAnalysisActivity
        final Button statisticalAnalyzeButton = (Button) findViewById(R.id.statistical_analyze_button);
        statisticalAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StatisticalAnalysisActivity.class);
                startActivity(intent);
            }
        });

        //click to move WorkOutHistoryActivity
        final Button acitivityHistoryButton = (Button) findViewById(R.id.acitivity_history_button);
        acitivityHistoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WorkOutHistoryActivity.class);
                startActivity(intent);
            }
        });

        //click to move WorkOutActivity
        final Button button = (Button) findViewById(R.id.acitivity_history_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WorkOutHistoryActivity.class);
                startActivity(intent);
            }
        });

        // click to move SettingsActivity
        final Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        //click to move WorkOutActivity
        final Button startActivityButton = (Button) findViewById(R.id.start_activity_button);
        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WorkOutActivity.class);
                startActivity(intent);
                //make vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(300);
            }
        });
    }
}