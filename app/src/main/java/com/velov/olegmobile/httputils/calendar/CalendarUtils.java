package com.velov.olegmobile.httputils.calendar;

import com.velov.olegmobile.httputils.HttpUtils;

import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CalendarUtils extends HttpUtils {
    /**
     * URL will be build for concrete date/week
     */
    public static String CALENDAR_URL = "https://olegbackend.ru/api_booking/event/calendar?cal_date=2022-05-26"; //Динамически вставлять дату

    public static Request buildGetRequest(OkHttpClient client, URL url, String token) {

        /**
         * Method for building GET request for calendar data
         */
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Connection", "keep-alive")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return request;
    }
}
