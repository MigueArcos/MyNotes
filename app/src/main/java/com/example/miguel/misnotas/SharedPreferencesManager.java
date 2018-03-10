package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 79812 on 10/03/2018.
 */

public class SharedPreferencesManager {
    private static SharedPreferencesManager sharedPreferencesManager;
    private SharedPreferences sharedPreferencesSettings;
    public static final String SETTINGS = "settings";
    public static final String SETTINGS_LAST_SELECTED_FRAGMENT = "lastSelectedFragment";

    private SharedPreferences sharedPreferencesSync;
    public static final String SYNC = "sync";
    public static final String SYNC_LAST_SYNCED_ID = "lastSyncedId";
    public static final String SYNC_TIME = "syncTime";
    public static final String SYNC_EMAIL = "email";
    public static final String SYNC_USER_ID = "userId";
    public static final String SYNC_USERNAME = "username";


    private SharedPreferencesManager(Context context) {
        sharedPreferencesSettings = context.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
        sharedPreferencesSync = context.getSharedPreferences(SYNC, Activity.MODE_PRIVATE);
    }

    //The context passed into the getInstance should be application level context.
    public static SharedPreferencesManager getInstance(Context context) {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return sharedPreferencesManager;
    }

    public void writeValue(String fileName, String key, String value) {
        switch (fileName) {
            case SETTINGS:
                sharedPreferencesSettings.edit().putString(key, value).apply();
                break;
            case SYNC:
                sharedPreferencesSync.edit().putString(key, value).apply();
                break;
        }
    }

    public String getValue(String fileName, String key) {
        switch (fileName) {
            case SETTINGS:
                return sharedPreferencesSettings.getString(key, "");
            case SYNC:
                return sharedPreferencesSync.getString(key, "");
        }
        return null;
    }
    public void writeValue(String fileName, String key, int value){
        switch (fileName){
            case SETTINGS:
                sharedPreferencesSettings.edit().putInt(key, value).apply();
                break;
            case SYNC:
                sharedPreferencesSync.edit().putInt(key, value).apply();
                break;
        }
    }

    public void writeValue(String fileName, String key, boolean value){
        switch (fileName){
            case SETTINGS:
                sharedPreferencesSettings.edit().putBoolean(key, value).apply();
                break;
            case SYNC:
                sharedPreferencesSync.edit().putBoolean(key, value).apply();
                break;
        }
    }

    public void writeValue(String fileName, String key, long value){
        switch (fileName){
            case SETTINGS:
                sharedPreferencesSettings.edit().putLong(key, value).apply();
                break;
            case SYNC:
                sharedPreferencesSync.edit().putLong(key, value).apply();
                break;
        }
    }

}