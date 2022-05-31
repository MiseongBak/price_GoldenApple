package com.aaa.aaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class OnionActivity extends AppCompatActivity {
    private Button btn106;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onion);

        BarChart barChart = findViewById(R.id.barChart);

        //샘플 데이터
        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(03-11, 420));
        visitors.add(new BarEntry(03-12, 425));
        visitors.add(new BarEntry(03-13, 520));
        visitors.add(new BarEntry(03-14, 620));
        visitors.add(new BarEntry(03-15, 540));
        visitors.add(new BarEntry(03-16, 720));
        visitors.add(new BarEntry(03-17, 120));
        visitors.add(new BarEntry(03-18, 320));
        visitors.add(new BarEntry(03-19, 410));
        visitors.add(new BarEntry(03-20, 900));
        visitors.add(new BarEntry(03-21, 570));
        visitors.add(new BarEntry(03-22, 100));
        visitors.add(new BarEntry(03-23, 1900));
        visitors.add(new BarEntry(03-24, 306));
        visitors.add(new BarEntry(03-25, 257));


        BarDataSet barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData bardata = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(bardata);
        barChart.getDescription().setText("Bar Chart Example");
        barChart.animateY(2000);


        Button btn106 = (Button) findViewById(R.id.btn106);
        btn106.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OniontableActivity.class);
                startActivity(intent);
            }
        });
    }

}