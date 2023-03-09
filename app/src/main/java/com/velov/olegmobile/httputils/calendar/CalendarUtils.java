package com.velov.olegmobile.httputils.calendar;

import com.velov.olegmobile.httputils.HttpUtils;

import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CalendarUtils extends HttpUtils {
    public static String CALENDAR_URL = "https://olegbackend.ru/api_booking/event/calendar";

    public static Request buildRequest(OkHttpClient client, String json, URL url, String token) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON); //JSON deserialization

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return request;
    }
}
