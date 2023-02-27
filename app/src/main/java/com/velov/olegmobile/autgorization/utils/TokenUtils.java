package com.velov.olegmobile.autgorization.utils;

import static com.velov.olegmobile.autgorization.utils.RequestUtils.parseJSON;

import com.velov.olegmobile.autgorization.utils.login.LoginUtils;
import com.velov.olegmobile.autgorization.utils.register.RegisterUtils;

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

        Request request= new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-API-KEY", "oleg")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = client.newCall(request).execute();

        return "Response is: " + response.body().string();
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

        return response.body().string();
    }

}
