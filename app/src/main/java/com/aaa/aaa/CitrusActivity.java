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

import com.aaa.aaa.ml.PriceConvertedSesame;
import com.aaa.aaa.ml.PriceConvertedSheepcbge;
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

public class CitrusActivity extends AppCompatActivity {
    private Button btn6;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citrus);


        Button btn6 = (Button) findViewById(R.id.btn_result);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View view) {
                try {
                    PriceConvertedSesame model= PriceConvertedSesame.newInstance(getApplicationContext());

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
                    PriceConvertedSesame.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] confidences = outputFeature0.getFloatArray();
                    TextView textView = (TextView) findViewById(R.id.textView73);
                    System.out.println("output 출력: " + confidences[0]);
                    Log.d(this.getClass().getName(), (String) textView.getText());
                    textView.setText("깻잎 가격:  " + confidences[0] + "원");
                    //Toast.makeText(getActivity(), Float.toString(confidences[0]),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });
    }

}