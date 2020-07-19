package com.soumya.sethy.reactnativesetupandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Soumya Ranjan Sethy <sethy.soumyaranjan@gmail.com>
 */
public class SharedPref {

    private SharedPreferences preferences;

    public SharedPref(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putString(String key, String value) {
        checkForNull(key);
        checkForNull(value);
        preferences.edit().putString(key, value).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void checkForNull(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
    }

}