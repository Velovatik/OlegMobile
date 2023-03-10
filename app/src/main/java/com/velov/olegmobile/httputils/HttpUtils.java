package com.velov.olegmobile.httputils;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    /**
     * URL parser for request
     * @param stringUrl requires URI in String format
     * @return URL Object for request
     */
    public static URL generateURL(String stringUrl) {
        Uri builtUri = Uri.parse(stringUrl)
                .buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Method send http request
     * @param client httpclient name
     * @param request require prepared request
     * @return access token as string
     */
    public static Response makeRequest(OkHttpClient client, Request request) {
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            call.cancel();
        }
        return response;
    }
}
