package com.example.run4fun.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.run4fun.BuildConfig;

public class Pref {

    private  static final String PREF_FILE = BuildConfig.APPLICATION_ID.replace(".","_");
    private static SharedPreferences sharedPreferences = null;

    private static void openPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
    }

    //For string value
    public static String getValue(Context context, String key,String defaultValue) {
        Pref.openPref(context);
        String result = Pref.sharedPreferences.getString(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setValue(Context context, String key, String value) {
        Pref.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.commit();
        Pref.sharedPreferences = null;
    }

}
