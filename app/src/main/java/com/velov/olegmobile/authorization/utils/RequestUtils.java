package com.velov.olegmobile.authorization.utils;

import com.google.gson.Gson;

import java.net.URL;

public abstract class RequestUtils {
    protected static final String OLEG_API_BASE_URL = "https://olegbackend.ru";

    public abstract URL generateURL();

    public static String parseJSON(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }
}
