package com.velov.olegmobile.authorization.utils;

import static com.velov.olegmobile.authorization.utils.RequestUtils.parseJSON;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.velov.olegmobile.authorization.utils.login.LoginUtils;
import com.velov.olegmobile.authorization.utils.register.RegisterUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenUtils {
    //Login
    public static String getToken(String login, String password) throws IOException, JSONException {
        RequestUtils utils = new LoginUtils();
        URL url = utils.generateURL();

        User user = new User(login, password);

        String json = parseJSON(user);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();

        String message = response.body().string();
        JsonObject object = new Gson().fromJson(message, JsonObject.class);

        int statusCode = response.code();

        if (statusCode == 200) {
            return response.body().string();
        } else {
            return "ERROR";
        }
    }

    //Register
    public static String getToken(String name, String login, String password)
            throws IOException, JSONException {
        RequestUtils utils = new RegisterUtils();
        URL url = utils.generateURL();

        User user = new User(name, login, password);

        String json = parseJSON(user);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, JSON);

        Request request= new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();

        String message = response.body().string();
        JsonObject object = new Gson().fromJson(message, JsonObject.class);

        int statusCode = response.code();

        if (statusCode == 200) {
            return object.get("access_token").getAsString();
        } else {
            return "ERROR";
        }
    }
}
