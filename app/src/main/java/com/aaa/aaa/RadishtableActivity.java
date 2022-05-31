package com.aaa.aaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RadishtableActivity extends AppCompatActivity {
    private Button btn103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radishtable);

        Button btn103 = (Button) findViewById(R.id.btn103);
        btn103.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RadishActivity.class);
                startActivity(intent);
            }
        });
    }
}