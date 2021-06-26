package com.example.run4fun.util;

public class Calorie {
    public double calories;
    public double month;

    public Calorie(double calories,String date)
    {
        this.calories =calories;
        //split for hour and date
        String[] dateArray=date.split(" ");
        this.month = Double.parseDouble(dateArray[0].split("-")[1]);
    }
}
