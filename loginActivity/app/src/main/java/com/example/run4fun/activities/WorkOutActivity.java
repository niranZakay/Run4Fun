package com.example.run4fun.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.run4fun.R;
import com.example.run4fun.WorkOut;
import com.example.run4fun.db.DataAccess;
import com.example.run4fun.db.WorkOutSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorkOutActivity extends AppCompatActivity {
    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;

    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;

    private static String date = "date";
    private static String distance = "distance";
    private static String time = "time";

    private static String TAG = "WorkOutActivity:";


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
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickPause(View view)
    {
        //show resume button
        final Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setVisibility(View.VISIBLE);

        //hide pause button
        final Button pauseButton = findViewById(R.id.pause_button);
        pauseButton.setVisibility(View.GONE);

        running = false;
    }

    public void onClickResume(View view)
    {
        //show pause button
        final Button pauseButton = findViewById(R.id.pause_button);
        pauseButton.setVisibility(View.VISIBLE);

        //hide resume button
        final Button resumeButton = findViewById(R.id.resume_button);
        resumeButton.setVisibility(View.GONE);

        running = true;
    }

    public void onClickFinish(View view)
    {
        createAlertBeforeFinish();
    }

    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer()
    {
        running = true;
        // Get the text view.
        final TextView timeView
                = (TextView)findViewById(
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

            public void run()
            {

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

    public void createAlertBeforeFinish()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.alert_text));
        builder.setMessage(getString(R.string.alert_before_finish_text));

        builder.setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //save the workout in db sqlite
                DataAccess dataAccess= DataAccess.DataAccess(getApplicationContext(), WorkOutSchema.databaseName);
                boolean result = dataAccess.addWorkOut(date,distance,time);
                if(result)
                {
                    //save success
                    Log.i(TAG, "db save results: successfully");
                    Toast.makeText(getApplicationContext(), getString(R.string.save_db_success_text), Toast.LENGTH_SHORT).show();

                }
                else
                {
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

}
