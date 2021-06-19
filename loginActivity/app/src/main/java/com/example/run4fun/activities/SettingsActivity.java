package com.example.run4fun.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.run4fun.R;
import com.example.run4fun.util.SettingItem;
import com.example.run4fun.util.SettingsAdapter;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "Settings Activity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        adapterToListView();

    }

    public void adapterToListView()
    {
        // Construct the data source
        ArrayList<SettingItem> arrayOfSettingItem= new ArrayList<SettingItem>();
        //add items to listView
        arrayOfSettingItem.add(new SettingItem("User","luram"));
        arrayOfSettingItem.add(new SettingItem("Height","luram"));
        arrayOfSettingItem.add(new SettingItem("Weight","luram"));
        arrayOfSettingItem.add(new SettingItem("Age","luram"));
        // Create the adapter to convert the array to views
        SettingsAdapter adapter = new SettingsAdapter(this, arrayOfSettingItem);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.settings_listview);
        listView.setAdapter(adapter);

        //add listner for click on the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                Log.i(TAG, "click on listview with position: " +String.valueOf(itemPosition));
                switch (itemPosition) {
                    //user
                    case 0:
                        {
                            createAlertDialogToSettingItem("user","luram");
                    }
                    break;
                    //height
                    case 1:
                    {
                        createAlertDialogToSettingItem("height","luram");
                    }
                        break;
                    //weight
                    case 2:
                    {
                        createAlertDialogToSettingItem("weight","luram");
                    }
                        break;
                    //age
                    case 3:
                    {
                        createAlertDialogToSettingItem("age","luram");
                    }
                    break;
                    default:
                        break;
                }

            }
        });
    }

    public void createAlertDialogToSettingItem(String title,String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message+" :");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.d("", "Pin Value : " + value);
                return;
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        alert.show();
    }

    public void sharedPrefrances
}