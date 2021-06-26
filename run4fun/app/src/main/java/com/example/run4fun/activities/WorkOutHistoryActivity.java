package com.example.run4fun.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.run4fun.R;
import com.example.run4fun.WorkOut;
import com.example.run4fun.adapters.WorkOutHistoryAdapter;
import com.example.run4fun.WorkOutHistoryItem;
import com.example.run4fun.db.DataAccess;
import com.example.run4fun.db.WorkOutSchema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class WorkOutHistoryActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_history);

        // Construct the data source
        ArrayList<WorkOutHistoryItem> arrayOfWorkOutHistoryItems= new ArrayList<WorkOutHistoryItem>();
        //add items to listView

        //fetch all workouts from db
        DataAccess dataAccess= DataAccess.DataAccess(getApplicationContext(), WorkOutSchema.databaseName);
        List<WorkOut> workOuts = new ArrayList<>();
        workOuts= dataAccess.fetchWorkoutTable();

        for (WorkOut workout:workOuts)
        {
            arrayOfWorkOutHistoryItems.add(new WorkOutHistoryItem(getString(R.string.date_text), workout.date,getString(R.string.distance_text),workout.distance,getString(R.string.time_text),workout.time,getString(R.string.calories_text),workout.calories,getString(R.string.avg_pace_text),workout.avg,getString(R.string.coordinates_text),workout.coordinates));
        }

        // Create the adapter to convert the array to views
        WorkOutHistoryAdapter adapter = new WorkOutHistoryAdapter(this, arrayOfWorkOutHistoryItems);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.workout_history_listview);
        listView.setAdapter(adapter);


        //add listner for click on the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId) {
                //move date,time,distance,coordinates to WorkOutFinishActivity
                Intent intent = new Intent(getBaseContext(), WorkOutDataActivity.class);
                intent.putExtra(WorkOutFinishActivity.DATE,arrayOfWorkOutHistoryItems.get(itemPosition).dateValue);
                intent.putExtra(WorkOutFinishActivity.TIME,arrayOfWorkOutHistoryItems.get(itemPosition).timeValue);
                intent.putExtra(WorkOutFinishActivity.DISTANCE,arrayOfWorkOutHistoryItems.get(itemPosition).distanceValue);
                intent.putExtra(WorkOutFinishActivity.CALORIES,arrayOfWorkOutHistoryItems.get(itemPosition).caloriesValue);
                intent.putExtra(WorkOutFinishActivity.AVG_PACE,arrayOfWorkOutHistoryItems.get(itemPosition).avgPaceValue);
                intent.putExtra(WorkOutFinishActivity.COORDINATES,arrayOfWorkOutHistoryItems.get(itemPosition).coordinatesValue);
                startActivity(intent);
            }
        });
            }
    }
