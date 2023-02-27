package com.velov.olegmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        result = findViewById(R.id.tv_result);

        Intent intent = getIntent();

        if (intent.hasExtra("access_token")) {
            String accessToken = intent.getStringExtra("access_token");
            result.setText(accessToken);
        }
    }
}