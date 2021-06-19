package com.example.run4fun.util;

import com.example.run4fun.R;
import com.example.run4fun.activities.WorkOutHistoryActivity;

public class WorkOutHistoryItem {
    String dateKey;
    String distanceKey;
    String timeKey;
    String dateValue;
    String distanceValue;
    String timeValue;

    public WorkOutHistoryItem(String dateKey, String dateValue ,String distanceKey,String distanceValue,String timeKey ,String timeValue)
    {
        this.dateKey = dateKey;
        this.dateValue= dateValue;
        this.distanceKey = distanceKey;
        this.distanceValue = distanceValue;
        this.timeKey =timeKey;
        this.timeValue = timeValue;
    }
}
