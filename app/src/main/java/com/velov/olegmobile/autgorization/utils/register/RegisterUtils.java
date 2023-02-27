package com.velov.olegmobile.autgorization.utils.register;

import android.net.Uri;

import com.google.gson.Gson;
import com.velov.olegmobile.autgorization.utils.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class RegisterUtils extends RequestUtils {
    private static final String OLEG_API_BASE_URL = "https://olegbackend.ru";
    private static final String REGISTER_URL = "/api_users/users/register";

    @Override
    public URL generateURL(){
        Uri builtUri = Uri.parse(OLEG_API_BASE_URL + REGISTER_URL)
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
