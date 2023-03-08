package com.velov.olegmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.velov.olegmobile.R;

public class CalendarActivity extends AppCompatActivity {

    private TextView result;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        result = findViewById(R.id.tv_result);

        Intent intent = getIntent();

    }

}