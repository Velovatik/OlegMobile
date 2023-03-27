package com.velov.olegmobile.httputils.calendar;

import com.velov.olegmobile.httputils.HttpUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CalendarUtils extends HttpUtils {
    /**
     * URL will be build for concrete date/week
     */
    public static String CALENDAR_URL = "https://olegbackend.ru/api_booking/event/calendar";//Add date with query params
    //YYYY-MM-DD

    //Methods to get current date for query parameters
    public static String getCurrentDate() {
        Date now = new Date();
        return getISO8601Date(now);
    }

    private static String getISO8601Date(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        return dateFormat.format(date);
    }

//    private static String nextWeek() {
//
//    }
    //================================================

    /**
     * Method for building GET request for calendar data
     * with headers and query params
     */
    public static Request buildGetRequest(OkHttpClient client, String url, String token) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        httpBuilder.addQueryParameter("cal_date", getCurrentDate());

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Connection", "keep-alive")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return request;
    }
}
