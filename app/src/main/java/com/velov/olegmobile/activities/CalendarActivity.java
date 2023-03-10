package com.velov.olegmobile.activities;

import static com.velov.olegmobile.httputils.HttpUtils.generateURL;
import static com.velov.olegmobile.httputils.calendar.CalendarUtils.*;
import static com.velov.olegmobile.httputils.calendar.CalendarUtils.CALENDAR_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.velov.olegmobile.R;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarActivity extends AppCompatActivity {

    private TextView key;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    class CalendarRequest implements Runnable {
        String calendarData = null;
        @Override
        public void run() { //Background work instead of doInBackground()
            URL url = generateURL(CALENDAR_URL);
            OkHttpClient client = new OkHttpClient();
            SharedPreferences sh = getSharedPreferences("tokenSharedPrefs", MODE_PRIVATE);
            String token = sh.getString("token", "");
            Request request = buildRequest(client, url, token);
            Response response = makeRequest(client, request);
            try {
                calendarData = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                @Override
                public void run() {
                    key.setText(calendarData);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        key = findViewById(R.id.tv_result);

        CalendarRequest calendarRequest = new CalendarRequest();
        executor.execute(calendarRequest);
    }
}