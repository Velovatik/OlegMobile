package com.velov.olegmobile.autgorization.utils.login;

import android.net.Uri;

import com.google.gson.Gson;
import com.velov.olegmobile.autgorization.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginUtils extends RequestUtils {
    private static final String OLEG_API_BASE_URL = "https://olegbackend.ru";
    private static final String LOGIN_URL = "/api_users/users/login";

    @Override
    public URL generateURL(){
        Uri builtUri = Uri.parse(OLEG_API_BASE_URL + LOGIN_URL)
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
}
