package com.velov.olegmobile.httputils.calendar;

import com.velov.olegmobile.httputils.HttpUtils;

import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CalendarUtils extends HttpUtils {
    public static String CALENDAR_URL = "https://olegbackend.ru/api_booking/event/calendar?cal_date=2022-05-26";

    public static Request buildRequest(OkHttpClient client, URL url, String token) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Connection", "keep-alive")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return request;
    }
}
