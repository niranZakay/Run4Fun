package com.example.run4fun.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.run4fun.R;
import com.example.run4fun.WorkOut;
import com.example.run4fun.db.DataAccess;
import com.example.run4fun.db.WorkOutSchema;
import com.example.run4fun.util.Calorie;
import com.example.run4fun.util.Distance;
import com.example.run4fun.util.TempSum;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticalAnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_analysis);
        createPieChart();
        createBarChart();

    }

    public void createBarChart()
    {


        //for distance graph


        //fetch all distance with date from db
        DataAccess dataAccess= DataAccess.DataAccess(getApplicationContext(), WorkOutSchema.databaseName);
        List<Distance> distances = dataAccess.fetchDistance();
        String[] arrayMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        //get the current month
        Calendar calendar = Calendar.getInstance();
        double currentMonth= calendar.get(Calendar.MONTH)+1;

        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> distancesWorkouts = new ArrayList<>();

        //create list with all the months and init them in 0
        List<TempSum> sumDistanceMonth = new ArrayList<>();

        for (String month:arrayMonth)
        {
            sumDistanceMonth.add(new TempSum(0,month));
        }

        for (Distance distance:distances)
        {
            //in the last 6 month
            if(distance.month<=currentMonth&&distance.month>=currentMonth-5)
            {

                for (TempSum distanceSum: sumDistanceMonth)
                {
                    if(arrayMonth[(int) (distance.month-1)].equals(distanceSum.month))
                    {
                        distanceSum.value += distance.distance;
                    }
                }

            }
        }

        //set results to the graph
        for (TempSum distanceSum: sumDistanceMonth)
        {
            if(distanceSum.value>0)
            {
               distancesWorkouts.add(new BarEntry(sumDistanceMonth.indexOf(distanceSum), (float) distanceSum.value));
            }
        }



        XAxis xAxis = barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(arrayMonth));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        BarDataSet barDataSet = new BarDataSet(distancesWorkouts,getString(R.string.workouts_text));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.getDescription().setText(getString(R.string.month_text));
        barChart.setData(barData);
        barChart.getLegend().setEnabled(false);
//        barChart.getXAxis().setEnabled(false);
        barChart.animateY(2000);
    }

    public void createPieChart()
    {
        //for calories graph


        //fetch all workouts from db
        DataAccess dataAccess= DataAccess.DataAccess(getApplicationContext(), WorkOutSchema.databaseName);
        List<Calorie> calories = dataAccess.fetchCalories();
        String[] arrayMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        //get the current month
        Calendar calendar = Calendar.getInstance();
        double currentMonth= calendar.get(Calendar.MONTH)+1;
        ArrayList<PieEntry> pieCharts = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.pieChart);

        //create list with all the months and init them in 0
        List<TempSum> sumCaloriesMonth = new ArrayList<>();

        for (String month:arrayMonth)
        {
            sumCaloriesMonth.add(new TempSum(0,month));
        }



        for (Calorie calorie:calories)
        {
            //in the last 4 month
            if(calorie.month<=currentMonth&&calorie.month>=currentMonth-3)
            {

              for (TempSum calorieSum: sumCaloriesMonth)
              {
                  if(arrayMonth[(int) (calorie.month-1)].equals(calorieSum.month))
                  {
                      calorieSum.value += calorie.calories;
                  }
              }

            }
        }

        //set results to the graph
        for (TempSum calorieSum: sumCaloriesMonth)
        {
            //value = calories
            if(calorieSum.value>0)
            {
                pieCharts.add(new PieEntry((float) calorieSum.value, calorieSum.month));
            }
        }



        //set properties to the graph
        PieDataSet pieDataSet = new PieDataSet(pieCharts,"Month");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(getString(R.string.calories_text));
        pieChart.animate();

    }
}