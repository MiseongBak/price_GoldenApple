package com.aaa.aaa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aaa.aaa.ml.PriceConvertedRadish;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class RadishActivity extends AppCompatActivity {
    private Button btn102;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radish);

        /*
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


        Button btn102 = (Button) findViewById(R.id.btn102);
        btn102.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RadishtableActivity.class);
                startActivity(intent);
            }
        });

         */

        Button btn102 = (Button) findViewById(R.id.btn_result);
        btn102.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View view) {
                try {
                    PriceConvertedRadish model= PriceConvertedRadish.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 4}, DataType.FLOAT32);

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 4);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    EditText idEdit1 = (EditText) findViewById(R.id.editTextNumber34); // 평균 온도
                    String value1 = idEdit1.getText().toString();

                    EditText idEdit2 = (EditText) findViewById(R.id.editTextNumber35); // 최저 온도
                    String value2 = idEdit2.getText().toString();

                    EditText idEdit3 = (EditText) findViewById(R.id.editTextNumber36); // 최고 온도
                    String value3 = idEdit3.getText().toString();

                    EditText idEdit4 = (EditText) findViewById(R.id.editTextNumber37); // 강수량
                    String value4 = idEdit4.getText().toString();

                    byteBuffer.putFloat(Float.parseFloat((value1)));
                    byteBuffer.putFloat(Float.parseFloat((value2)));
                    byteBuffer.putFloat(Float.parseFloat((value3)));
                    byteBuffer.putFloat(Float.parseFloat((value4)));

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    PriceConvertedRadish.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] confidences = outputFeature0.getFloatArray();
                    TextView textView = (TextView) findViewById(R.id.textView73);
                    System.out.println("output 출력: " + confidences[0]);
                    Log.d(this.getClass().getName(), (String) textView.getText());
                    textView.setText("무 가격:  " + confidences[0] + "원");
                    //Toast.makeText(getActivity(), Float.toString(confidences[0]),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });



    }

}