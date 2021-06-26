package com.example.run4fun;

import com.example.run4fun.R;
import com.example.run4fun.activities.WorkOutHistoryActivity;

import java.util.List;

public class WorkOutHistoryItem {
    public String dateKey;
    public String distanceKey;
    public String timeKey;
    public String dateValue;
    public String distanceValue;
    public String timeValue;
    public String coordinatesKey;
    public String coordinatesValue;
    public String avgPaceKey;
    public String avgPaceValue;
    public String caloriesKey;
    public String caloriesValue;


    public WorkOutHistoryItem(String dateKey, String dateValue , String distanceKey, String distanceValue, String timeKey , String timeValue,String caloriesKey,String caloriesValue,String avgPaceKey,String avgPaceValue ,String coordinatesKey,String coordinatesValue)
    {
        this.dateKey = dateKey;
        this.dateValue= dateValue;
        this.distanceKey = distanceKey;
        this.distanceValue = distanceValue;
        this.timeKey =timeKey;
        this.timeValue = timeValue;
        this.coordinatesKey = coordinatesKey;
        this.coordinatesValue = coordinatesValue;
        this.caloriesKey = caloriesKey;
        this.caloriesValue =caloriesValue;
        this.avgPaceKey = avgPaceKey;
        this.avgPaceValue = avgPaceValue;
    }
}
