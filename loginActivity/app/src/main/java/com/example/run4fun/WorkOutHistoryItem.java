package com.example.run4fun;

import com.example.run4fun.R;
import com.example.run4fun.activities.WorkOutHistoryActivity;

public class WorkOutHistoryItem {
    public String dateKey;
    public String distanceKey;
    public String timeKey;
    public String dateValue;
    public String distanceValue;
    public String timeValue;

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
