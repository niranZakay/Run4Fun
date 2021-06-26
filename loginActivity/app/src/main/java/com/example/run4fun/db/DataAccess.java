package com.example.run4fun.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.run4fun.Coordinate;
import com.example.run4fun.WorkOut;
import com.example.run4fun.util.Calorie;
import com.example.run4fun.util.Distance;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//added
public class DataAccess {
    private static DataAccess single_instance=null;
    public DatabaseHelper helper;
    public SQLiteDatabase db;
    public Context context;


    private DataAccess(Context context, String dbName) {
        this.helper = new DatabaseHelper(context, dbName, null, 1);
        this.db = helper.getWritableDatabase();
        this.context = context;

    }

    public static DataAccess DataAccess(Context context, String dbName)
    {
    // To ensure only one instance is created
        if(single_instance ==null)
    {
        single_instance = new DataAccess(context,dbName);
    }
        return single_instance;
    }


    public boolean addWorkOut(String date, String distance, String time,String jsonCoordinates,String calories,String avgPace)
    {

        ContentValues vals = new ContentValues();
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DATE, date);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DISTANCE, distance);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_TIME, time);
        vals.put(WorkOutSchema.COLUMN_CALORIES, calories);
        vals.put(WorkOutSchema.COLUMN_AVGPACE, avgPace);
        vals.put(WorkOutSchema.COLUMN_COORDINATES, jsonCoordinates);
        long result = db.insert(WorkOutSchema.WORKOUTS_TABLE, "null", vals);
        if(result>0)
        {
            return true;
        }
        else
        {
            return false; //error occur
        }
    }

    public boolean addWorkOutTesting(String date, String distance, String time,String jsonCoordinates,String calories,String avgPace)
    {

        ContentValues vals = new ContentValues();
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DATE, date);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DISTANCE, distance);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_TIME, time);
        vals.put(WorkOutSchema.COLUMN_CALORIES, calories);
        vals.put(WorkOutSchema.COLUMN_AVGPACE, avgPace);
        vals.put(WorkOutSchema.COLUMN_COORDINATES, jsonCoordinates);
        long result = db.insert(WorkOutSchema.WORKOUTS_TABLE, "null", vals);
        if(result>0)
        {
            return true;
        }
        else
        {
            return false; //error occur
        }
    }

    public List<WorkOut> fetchWorkoutTable()
    {
        List<WorkOut> workoOutList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(WorkOutSchema.WORKOUTS_TABLE,new String[]{WorkOutSchema.COLUMN_WORKOUT_DATE,WorkOutSchema.COLUMN_WORKOUT_DISTANCE,WorkOutSchema.COLUMN_WORKOUT_TIME,WorkOutSchema.COLUMN_CALORIES,WorkOutSchema.COLUMN_AVGPACE,WorkOutSchema.COLUMN_COORDINATES},"",null,"","","");
        while(cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DATE));
            String distance = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DISTANCE));
            String time = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_TIME));
            String calories = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_CALORIES));
            String avg = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_AVGPACE));
            String coordinates = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_COORDINATES));
            WorkOut workoOut = new WorkOut(date,distance,time, coordinates,calories,avg);
            workoOutList.add(workoOut);
        }
        cursor.close();
    return workoOutList;
    }


    public List<Calorie> fetchCalories()
    {
        List<Calorie> caloriesList = new ArrayList<>();
        //get the current month
        Calendar calendar = Calendar.getInstance();
        int month= calendar.get(Calendar.MONTH);

        SQLiteDatabase sqLiteDatabase= helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(WorkOutSchema.WORKOUTS_TABLE,new String[]{WorkOutSchema.COLUMN_CALORIES,WorkOutSchema.COLUMN_WORKOUT_DATE},"",null,"","","");
        while(cursor.moveToNext())
        {
            Double calories = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_CALORIES)));
            String date = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DATE));
            caloriesList.add(new Calorie(calories,date));
        }
        cursor.close();
        return caloriesList;
    }

    public List<Distance> fetchDistance()
    {
        List<Distance> distanceList = new ArrayList<>();
        //get the current month
        Calendar calendar = Calendar.getInstance();
        int month= calendar.get(Calendar.MONTH);

        SQLiteDatabase sqLiteDatabase= helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(WorkOutSchema.WORKOUTS_TABLE,new String[]{WorkOutSchema.COLUMN_WORKOUT_DISTANCE,WorkOutSchema.COLUMN_WORKOUT_DATE},"",null,"","","");
        while(cursor.moveToNext())
        {
            Double distance = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DISTANCE)));
            String date = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DATE));
            distanceList.add(new Distance(distance,date));
        }
        cursor.close();
        return distanceList;
    }


    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String
                name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(WorkOutSchema.CREATE_TABLE_WORKOUTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+WorkOutSchema.WORKOUTS_TABLE);
            onCreate(db);


        }

    }

}