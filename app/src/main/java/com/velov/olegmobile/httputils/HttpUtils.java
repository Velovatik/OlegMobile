package com.velov.olegmobile.httputils;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.velov.olegmobile.httputils.authorization.Status;

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
    static String message = null;

    /**
     * Getter for field
     * @return
     */
    public static String getMessage() { //Getter in case request is already closed
        return message;
    }

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
     * @return access_token param as String
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

    /**
     * Middleware layer for getting response status and validate fields
     * Cycling through enum and show status
     * @param response use response from server for status recognition
     * @return enum value that shows is everything OK
     */
    public static Status getStatus(Response response) {
        int statusCode = 0;
        String token = null;
        Status status = Status.DEFAULT;
        try{
            message = response.body().string();
        } catch (IOException | NullPointerException e) {
            return Status.CONNECTION_ERROR;
        }
        statusCode = response.code();

        if (statusCode == 200) {
            return Status.OK;
        } else if (statusCode == 422) { //Condition for invalid token
            return Status.TOKEN_ERROR;
        } else {
            return Status.ERROR;
        }
    }

    /**
     * Method returns JsonObject for getting some params from it
     * @param response
     * @return
     */
    public static JsonObject getResponseJSON(Response response) {
        String message = getMessage();
        JsonObject responseJSON = new Gson().fromJson(message, JsonObject.class);
        return responseJSON;
    }
}
