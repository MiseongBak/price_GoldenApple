package com.aaa.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OniontableActivity extends AppCompatActivity {
    private Button btn107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oniontable);

        Button btn107 = (Button) findViewById(R.id.btn107);
        btn107.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OnionActivity.class);
                startActivity(intent);
            }
        });
    }
}