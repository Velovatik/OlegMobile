package com.velov.olegmobile.httputils.calendar;

import com.velov.olegmobile.httputils.HttpUtils;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CalendarUtils extends HttpUtils {
    /**
     * URL will be build for concrete date/week
     */
    public static String CALENDAR_URL = "https://olegbackend.ru/api_booking/event/calendar?cal_date=2022-05-26"; //Динамически вставлять дату
    //YYYY-MM-DD

    //Methods to get current data for query parameters
    public static String getCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        return dateFormat.format(date);
    }
    //===============================================

    /**
     * Method for building GET request for calendar data
     */
    public static Request buildGetRequest(OkHttpClient client, URL url, String token) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Connection", "keep-alive")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return request;
    }
}
