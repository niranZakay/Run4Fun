package com.example.run4fun;

import java.util.List;

public class WorkOut {
    public String date;
    public String distance;
    public String time;
    public String coordinates;
    public String calories;
    public String avg;

    public WorkOut(String date, String distance, String time,String coordinates,String calories,String avg)
    {
        this.date= date;
        this.distance= distance;
        this.time = time;
        this.calories=calories;
        this.avg= avg;
        this.coordinates = coordinates;
    }

    public WorkOut() {

    }
}
