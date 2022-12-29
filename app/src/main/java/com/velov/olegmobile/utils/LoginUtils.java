package com.velov.olegmobile.utils;

import android.net.Uri;

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

public class LoginUtils {
    private static final String OLEG_API_BASE_URL = "https://olegbackend.ru";
    private static final String LOGIN_API_URL = "/api_users/users/login";

    final static String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";


    public static URL generateLoginURL(){
        Uri builtLoginUri = Uri.parse(OLEG_API_BASE_URL + LOGIN_API_URL)
                .buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtLoginUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getToken(String login, String password) throws IOException, JSONException {

        URL url = generateLoginURL();

        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("X-API-KEY", "oleg");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("User-Agent", USER_AGENT);

        con.setDoOutput(true);

        String jsonInputString = parseJSON(login, password);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            con.disconnect();
        }
    }

    public static String parseJSON(String login, String password) throws JSONException {
        String loginData = new JSONObject()
                .put("login", login)
                .put("password", password)
                .toString();

        return loginData;
    }
}
