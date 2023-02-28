package com.velov.olegmobile.authorization.utils.login;

import android.net.Uri;

import com.velov.olegmobile.authorization.utils.RequestUtils;

import java.net.MalformedURLException;
import java.net.URL;

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
