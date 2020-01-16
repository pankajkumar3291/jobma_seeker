package com.smartit.jobSeeker.sharedPreferenceUtils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefsHelper {

    private SharedPreferences mSharedPreferences;
    public static final String PREFS_NAME = "PROVIDER_PREFERENCE";


    public SharedPrefsHelper(Context context) {
        this.mSharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);

    }


    public void put(String key, String value) {

        mSharedPreferences.edit().putString(key, value).apply();
    }


    public void put(String key, int value) {

        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {

        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }
}
