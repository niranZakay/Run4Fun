package com.example.run4fun.util;

public class Distance
{


public double distance;
public double month;

public Distance(double distance,String date)
        {
        this.distance =distance;
        //split for hour and date
        String[] dateArray=date.split(" ");
        this.month = Double.parseDouble(dateArray[0].split("-")[1]);
        }

}