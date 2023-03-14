package com.velov.olegmobile.middleware;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.velov.olegmobile.activities.LoginActivity;

import java.util.Objects;

public class SharedPreferencesStorage {
    private Context context;



    public boolean existingTokenCheck(Context context, SharedPreferences preferences) {
        preferences = context.getSharedPreferences("access_token", MODE_PRIVATE);
        String access_token = preferences.getString("token", "undefined");
        if (!Objects.equals(access_token, "undefined")) {
            return false;
        }
        else return true;
    }

    public void writeTokenToCookies(Context context, SharedPreferences preferences, String value) {
        preferences = context.getSharedPreferences("access_token", MODE_PRIVATE); //access_token is name of prefs file
        SharedPreferences.Editor tokenEditor = preferences.edit();
        tokenEditor.putString("token", value);
        tokenEditor.apply();
    }
}
