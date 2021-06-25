package com.example.run4fun.db;

public class WorkOutSchema {

    public final static String databaseName = "workoutdatabase.db";
    public final static String WORKOUTS_TABLE = "Workouts";
    public final static String WORKOUTS_ID = "_id";
    public final static String COLUMN_WORKOUT_DATE = "date";
    public final static String COLUMN_WORKOUT_DISTANCE = "distance";
    public final static String COLUMN_WORKOUT_TIME = "time";
    public final static String COLUMN_COORDINATES = "Coordinate";


    protected final static String CREATE_TABLE_WORKOUTS ="CREATE TABLE "+ WORKOUTS_TABLE+ "("+
            WORKOUTS_ID+" INTEGER PRIMARY KEY,"+
            COLUMN_WORKOUT_DATE+" TEXT NOT NULL,"+
            COLUMN_WORKOUT_DISTANCE+" TEXT NOT NULL,"+
            COLUMN_WORKOUT_TIME+" TEXT NOT NULL,"+
            COLUMN_COORDINATES+" TEXT NOT NULL)";


}
