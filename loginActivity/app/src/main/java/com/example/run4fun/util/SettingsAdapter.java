package com.example.run4fun.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.example.run4fun.R;
import com.example.run4fun.util.SettingItem;

import java.util.ArrayList;

public class SettingsAdapter extends ArrayAdapter<SettingItem> {
    public SettingsAdapter(Context context, ArrayList<SettingItem> SettingItems) {
        super(context, 0, SettingItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SettingItem settingItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.key_textview);
        TextView tvHome = (TextView) convertView.findViewById(R.id.value_textview);
        // Populate the data into the template view using the data object
        tvName.setText(settingItem.key);
        tvHome.setText(settingItem.value);
        // Return the completed view to render on screen
        return convertView;
    }
}