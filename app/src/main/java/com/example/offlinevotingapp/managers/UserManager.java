package com.example.offlinevotingapp.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_NAME = "user_name";

    private final SharedPreferences prefs;

    public UserManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public boolean isRegistered() {
        return !getUserName().isEmpty();
    }
}
