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
    private Spinner genderSpinner;
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
        arrayOfSettingItem.add(new SettingItem(getString(R.string.gender_text),Pref.getValue(this,"gender", "Male")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.height_text),Pref.getValue(this,"height", "175")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.weight_text),Pref.getValue(this,"weight", "70")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.age_text),Pref.getValue(this,"age", "24")));
        arrayOfSettingItem.add(new SettingItem(getString(R.string.bmr_text),Pref.getValue(this,"bmr", "0")));
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

                    //gender
                    case 0:
                    {
                        createAlertDialogWithRadioButtonToSettingItem();

                    }
                    break;
                    //height
                    case 1:
                    {
                        createAlertDialogWithEditTextToSettingItem(getString(R.string.height_text),getString(R.string.please_enter_text) +" "+getString(R.string.height_text));

                    }
                        break;
                    //weight
                    case 2:
                    {
                        createAlertDialogWithEditTextToSettingItem(getString(R.string.weight_text),getString(R.string.please_enter_text)+" "+getString(R.string.weight_text));
                    }
                        break;
                    //age
                    case 3:
                    {
                        createAlertDialogWithEditTextToSettingItem(getString(R.string.age_text),getString(R.string.please_enter_text )+" "+getString(R.string.age_text));
                    }
                    break;

                    default:
                        break;
                }


            }
        });
    }

    public void createAlertDialogWithEditTextToSettingItem(String title, String message)
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
                calculateBmr();
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

    private void createAlertDialogWithRadioButtonToSettingItem() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle("Gender");
        String[] items = {"Male","Female","Other"};
        int checkedItem = 0;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Pref.setValue(getApplicationContext(),"gender",getString(R.string.male_text));
                        Toast.makeText(getApplicationContext(),"'"+"Gender" +"' change to '"+getString(R.string.male_text)+"' successfully",Toast.LENGTH_SHORT).show();
                        calculateBmr();
                        break;
                    case 1:
                        Pref.setValue(getApplicationContext(),"gender",getString(R.string.female_text));
                        Toast.makeText(getApplicationContext(),"'"+"Gender" +"' change to '"+getString(R.string.female_text)+"' successfully",Toast.LENGTH_SHORT).show();
                        calculateBmr();
                        break;
                    case 2:
                        Pref.setValue(getApplicationContext(),"gender",getString(R.string.other_text));
                        Toast.makeText(getApplicationContext(),"'"+"Gender" +"' change to '"+getString(R.string.other_text)+"' successfully",Toast.LENGTH_SHORT).show();
                        calculateBmr();
                        break;

                }
                dialog.dismiss();
                restartActivity();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void restartActivity() {
        Intent starterIntent = getIntent();
        finish();
        startActivity(starterIntent);
    }

    private void calculateBmr()
    {
        //by https://www.omnicalculator.com/health/bmr-harris-benedict-equation#what-is-a-bmr-calculator
       String gender = Pref.getValue(this,"gender", "Male");
       double height =Double.parseDouble(Pref.getValue(this,"height", "175"));
       double weight = Double.parseDouble(Pref.getValue(this,"weight", "70"));
       double age = Double.parseDouble(Pref.getValue(this,"age", "24"));
       double bmr = 0;
       double constGender;

       double CONST_AGE = 5;
       double CONST_HEIGHT = 6.25;
       double CONST_WEIGHT = 10;
       double CONST_MALE = 5;
       double CONST_FEMALE = -161;



       if(gender.toLowerCase().equals("male"))
       {
           constGender = CONST_MALE;
       }
       else
       {
           constGender= CONST_FEMALE;
       }

        bmr = (CONST_WEIGHT*weight)+(height*CONST_HEIGHT)-(age*CONST_AGE)+constGender;
        Pref.setValue(this,"bmr",String.valueOf(bmr));
    }

}