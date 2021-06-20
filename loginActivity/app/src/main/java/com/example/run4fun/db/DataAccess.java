package com.example.run4fun.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.run4fun.WorkOut;

import java.util.ArrayList;
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


    public boolean addWorkOut(String date, String distance, String time)
    {

        ContentValues vals = new ContentValues();
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DATE, date);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_DISTANCE, distance);
        vals.put(WorkOutSchema.COLUMN_WORKOUT_TIME, time);
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
        Cursor cursor = sqLiteDatabase.query(WorkOutSchema.WORKOUTS_TABLE,new String[]{WorkOutSchema.COLUMN_WORKOUT_DATE,WorkOutSchema.COLUMN_WORKOUT_DISTANCE,WorkOutSchema.COLUMN_WORKOUT_TIME},"",null,"","","");
        while(cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DATE));
            String distance = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_DISTANCE));
            String time = cursor.getString(cursor.getColumnIndex(WorkOutSchema.COLUMN_WORKOUT_TIME));
            WorkOut workoOut = new WorkOut(date,distance,time);
            workoOutList.add(workoOut);
        }
        cursor.close();
    return workoOutList;
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