package com.velov.olegmobile.authorization.utils.register;

import android.net.Uri;

import com.velov.olegmobile.authorization.utils.RequestUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class RegisterUtils extends RequestUtils {
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
