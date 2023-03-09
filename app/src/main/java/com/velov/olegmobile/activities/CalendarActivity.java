package com.velov.olegmobile.activities;

import static com.velov.olegmobile.httputils.HttpUtils.generateURL;
import static com.velov.olegmobile.httputils.calendar.CalendarUtils.CALENDAR_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.velov.olegmobile.R;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity {

    private TextView key;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    class CalendarRequest implements Runnable {
        @Override
        public void run() { //Background work instead of doInBackground()
            URL url = generateURL(CALENDAR_URL);
            handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        key = findViewById(R.id.tv_result);

        SharedPreferences sh = getSharedPreferences("tokenSharedPrefs", MODE_PRIVATE);
        String token = sh.getString("token", "");
        key.setText(token);
    }

}