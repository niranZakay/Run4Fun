package com.example.run4fun.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.run4fun.R;
import com.example.run4fun.adapters.WorkOutHistoryAdapter;
import com.example.run4fun.WorkOutHistoryItem;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class WorkOutHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_history);

        // Construct the data source
        ArrayList<WorkOutHistoryItem> arrayOfWorkOutHistoryItems= new ArrayList<WorkOutHistoryItem>();
        //add items to listView
        arrayOfWorkOutHistoryItems.add(new WorkOutHistoryItem(getString(R.string.date_text), "luram ipsum",getString(R.string.distance_text),"lutram ipsum",getString(R.string.time_text),"lutram ipsim"));
        arrayOfWorkOutHistoryItems.add(new WorkOutHistoryItem(getString(R.string.date_text), "luram ipsum",getString(R.string.distance_text),"lutram ipsum",getString(R.string.time_text),"lutram ipsim"));
        arrayOfWorkOutHistoryItems.add(new WorkOutHistoryItem(getString(R.string.date_text), "luram ipsum",getString(R.string.distance_text),"lutram ipsum",getString(R.string.time_text),"lutram ipsim"));
        arrayOfWorkOutHistoryItems.add(new WorkOutHistoryItem(getString(R.string.date_text), "luram ipsum",getString(R.string.distance_text),"lutram ipsum",getString(R.string.time_text),"lutram ipsim"));
        // Create the adapter to convert the array to views
        WorkOutHistoryAdapter adapter = new WorkOutHistoryAdapter(this, arrayOfWorkOutHistoryItems);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.workout_history_listview);
        listView.setAdapter(adapter);
    }
}