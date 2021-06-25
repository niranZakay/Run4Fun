package com.example.run4fun.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.run4fun.R;
import com.example.run4fun.util.Pref;
import com.example.run4fun.SettingItem;
import com.example.run4fun.adapters.SettingsAdapter;

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
        arrayOfSettingItem.add(new SettingItem(getString(R.string.height_text),Pref.getValue(this,"height", "175")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.weight_text),Pref.getValue(this,"weight", "70")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.age_text),Pref.getValue(this,"age", "24")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.bmr_text),Pref.getValue(this,"BMR", "24")));
        // Create the adapter to convert the array to views
        SettingsAdapter adapter = new SettingsAdapter(this, arrayOfSettingItem);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.settings_listview);
        listView.setAdapter(adapter);

        //get the spinner from the xml.
        Spinner genderSpinner = findViewById(R.id.spinner);
//create a list of items for the spinner.
        String[] items = new String[]{getString(R.string.male_text),getString(R.string.female_text)};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterForSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        genderSpinner.setAdapter(adapterForSpinner);
        genderSpinner.setPrompt(Pref.getValue(this,"gender", "Male"));

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                        Spinner genderSpinner = findViewById(R.id.spinner);
                                                        Pref.setValue(getApplicationContext(),"gender",parentView.getItemAtPosition(position).toString());
                                                        genderSpinner.setPrompt(Pref.getValue(getApplicationContext(),"gender", parentView.getItemAtPosition(position).toString()));
                                                        Toast.makeText(getApplicationContext(),"'"+"Gender" +"' change to '"+parentView.getItemAtPosition(position).toString()+"' successfully",Toast.LENGTH_SHORT).show();
//                                                        restartActivity();
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parentView) {
                                                        // your code here
                                                    }
                                                });

        //add listner for click on the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                Log.i(TAG, "click on listview with position: " +String.valueOf(itemPosition));
                switch (itemPosition) {

                    //height
                    case 0:
                    {
                        createAlertDialogToSettingItem(getString(R.string.height_text),getString(R.string.please_enter_text) +" "+getString(R.string.height_text));
                    }
                        break;
                    //weight
                    case 1:
                    {
                        createAlertDialogToSettingItem(getString(R.string.weight_text),getString(R.string.please_enter_text)+" "+getString(R.string.weight_text));
                    }
                        break;
                    //age
                    case 2:
                    {
                        createAlertDialogToSettingItem(getString(R.string.age_text),getString(R.string.please_enter_text )+" "+getString(R.string.age_text));
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
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Pref.setValue(getApplicationContext(),title.toLowerCase(),value);
                Toast.makeText(getApplicationContext(),"'"+title +"' change to '"+value+"' successfully",Toast.LENGTH_SHORT).show();
                restartActivity();
            }
        });

        alert.setNegativeButton(getString(R.string.cancel_text),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        alert.show();
    }

    private void restartActivity() {
        Intent starterIntent = getIntent();
        finish();
        startActivity(starterIntent);
    }

}