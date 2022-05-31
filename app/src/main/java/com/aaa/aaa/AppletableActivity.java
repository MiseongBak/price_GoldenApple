package com.aaa.aaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppletableActivity extends AppCompatActivity {
    private Button btn101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apptable);

        Button btn101 = (Button) findViewById(R.id.btn101);
        btn101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AppleActivity.class);
                startActivity(intent);
            }
        });
    }
}