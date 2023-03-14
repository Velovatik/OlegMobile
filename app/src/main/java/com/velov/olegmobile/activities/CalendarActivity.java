package com.velov.olegmobile.activities;

import static com.velov.olegmobile.httputils.HttpUtils.generateURL;
import static com.velov.olegmobile.httputils.authorization.TokenUtils.getStatus;
import static com.velov.olegmobile.httputils.calendar.CalendarUtils.*;
import static com.velov.olegmobile.httputils.calendar.CalendarUtils.CALENDAR_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.velov.olegmobile.R;
import com.velov.olegmobile.httputils.authorization.Status;

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
        private Status status = Status.DEFAULT;

        public Status getRequestStatus() {
            return status;
        }

        String calendarData = null;

        /**
         * Method invoke return to login page
         */
        public void goToLogin() {
            Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        @Override
        public void run() { //Background work instead of doInBackground()
            //Make request
            URL url = generateURL(CALENDAR_URL);
            OkHttpClient client = new OkHttpClient();
            SharedPreferences sh = getSharedPreferences("access_token", MODE_PRIVATE);
            String token = sh.getString("token", "undefined"); //fix
            Request request = buildGetRequest(client, url, token);
            Response response = makeRequest(client, request);

            status = getStatus(response);

            try {
                switch (status) {
                    case OK: {
                        try {
                            calendarData = response.body().string();
                        } catch (IOException | NullPointerException e) {
                            goToLogin();
                        }
                        break;
                    } default: {
                        goToLogin();
                    }
                }
            } finally {
                handler.post(new Runnable() { //UI Thread instead of onPostExecute()
                    @Override
                    public void run() {
                        key.setText(calendarData);
                    }
                });
            }
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