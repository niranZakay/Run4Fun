package com.example.run4fun.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.run4fun.R;
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
        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> workouts = new ArrayList<>();
        workouts.add(new BarEntry(1,420));
        workouts.add(new BarEntry(2,10));
        workouts.add(new BarEntry(3,3700));
        workouts.add(new BarEntry(4,420));
        workouts.add(new BarEntry(5,420));
        workouts.add(new BarEntry(6,420));


        XAxis xAxis = barChart.getXAxis();

        final String[] labels = new String[] {"Dummy", "Jan", "Feb", "March", "April", "May",
                "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        BarDataSet barDataSet = new BarDataSet(workouts,getString(R.string.workouts_text));
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
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> pieCharts = new ArrayList<>();
        pieCharts.add(new PieEntry(500,"Jan"));
        pieCharts.add(new PieEntry(500,"Feb"));
        pieCharts.add(new PieEntry(200,"Mar"));
        pieCharts.add(new PieEntry(300,"Apr"));

        PieDataSet pieDataSet = new PieDataSet(pieCharts,"Month");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(getString(R.string.distance_text));
        pieChart.animate();

    }
}