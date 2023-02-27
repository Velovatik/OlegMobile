package com.velov.olegmobile.autgorization.utils;

import android.net.Uri;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class RequestUtils {

    public abstract URL generateURL();

    public static String parseJSON(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
