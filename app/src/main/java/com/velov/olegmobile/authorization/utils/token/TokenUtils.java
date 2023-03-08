package com.velov.olegmobile.authorization.utils.token;

import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.velov.olegmobile.authorization.utils.User;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenUtils {
    public static final String OLEG_LOGIN_URL = "https://olegbackend.ru/api_users/users/login";
    public static final String OLEG_REGISTER_URL =
            "https://olegbackend.ru/api_users/users/register";

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
     * Method for parsing JSON
     * @param login user's login
     * @param password user's password
     * @return User Object for login operation calling constructor
     * with two fields
     */
    public static User getUserData(String login, String password) {
        User user = new User(login, password);

        return user;
    }

    /**
     * Overloaded method for parsing JSON
     * @param name username for new user
     * @param login login for new user
     * @param password password for new user
     * @return User Object for registration from constructor
     * with three fields
     */
    public static User getUserData(String name, String login, String password) {
        User user = new User(name,login, password);

        return user;
    }

    /**
     * @param user require User Object for automatic creation JSON string
     * @return serialized JSON in String format for request body
     */
    public static String parseJSON(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    /**
     * Uses okhttp framework for request
     * @param json require String for building request
     * @return Request Object for request sending
     */
    public static Request buildRequest(OkHttpClient client, String json, URL url) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON); //JSON deserialization

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Content-Type", "application/json")
                .build();

        return request;
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

    static String message = null;

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
        } else {
            return Status.ERROR;
        }
    }

    /**
     * if getStatus() returns Status.OK it returns token for further authorization
     * @param response use response from server for JSON serialization key from "access_token" value
     * @return serialized access_token
     */
    public static String getToken(Response response) {
        JsonObject responseJSON = new Gson().fromJson(message, JsonObject.class);
        String token = responseJSON.get("access_token").getAsString();
        return token;
    }
}
