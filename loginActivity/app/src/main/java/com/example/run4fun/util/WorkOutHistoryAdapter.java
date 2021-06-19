package com.example.run4fun.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.run4fun.R;

import java.util.ArrayList;


    public class WorkOutHistoryAdapter extends ArrayAdapter<WorkOutHistoryItem> {
        public WorkOutHistoryAdapter(Context context, ArrayList<WorkOutHistoryItem> workOutHistoryItems) {
            super(context, 0, workOutHistoryItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            WorkOutHistoryItem workOutHistoryItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.workout_history_layout, parent, false);
            }
            // Lookup view for data population
            TextView tvDateKey= (TextView) convertView.findViewById(R.id.date_key_edit_text);
            TextView tvDistanceKey = (TextView) convertView.findViewById(R.id.distance_key_edit_text);
            TextView tvTimeKey = (TextView) convertView.findViewById(R.id.time_key_edit_text);
            TextView tvDateValue= (TextView) convertView.findViewById(R.id.date_value_edit_text);
            TextView tvDistanceValue = (TextView) convertView.findViewById(R.id.distatnce_value_edit_text);
            TextView tvTimeValue = (TextView) convertView.findViewById(R.id.time_value_edit_text);

            // Populate the data into the template view using the data object
            tvDateKey.setText(workOutHistoryItem.dateKey);
            tvDistanceKey.setText(workOutHistoryItem.distanceKey);
            tvTimeKey.setText(workOutHistoryItem.timeKey);
            tvDateValue.setText(workOutHistoryItem.dateValue);
            tvDistanceValue.setText(workOutHistoryItem.distanceValue);
            tvTimeValue.setText(workOutHistoryItem.timeValue);
            // Return the completed view to render on screen
            return convertView;
        }

}
