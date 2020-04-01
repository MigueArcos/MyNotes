package com.zeus.migue.notes.infrastructure.services.implementations;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.zeus.migue.notes.data.DTO.SignInResponse;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.Date;

public class UserPreferences {
    private static UserPreferences userPreferences;
    private SharedPreferences userInfo;
    private SharedPreferences appSettings;

    private static class UserInformationKeys{
        private static final String preferencesName = "userInfo";
        private static final String email = "email";
        private static final String userName = "userName";
        private static final String userId = "userId";
        private static final String token = "token";
        private static final String refreshToken = "refreshToken";
        private static final String expireDate = "expireDate";
    }

    private class AppSettingsKeys{
        private static final String preferencesName = "appSettings";
        private static final String lastSelectedFragment = "lastSelectedFragment";
        private static final String lastSyncTime = "lastSyncTime";
    }


    private UserPreferences(Context appContext) {
        userInfo = appContext.getSharedPreferences(UserInformationKeys.preferencesName, AppCompatActivity.MODE_PRIVATE);
        appSettings = appContext.getSharedPreferences(AppSettingsKeys.preferencesName, AppCompatActivity.MODE_PRIVATE);
    }

    //The context passed into the getInstance should be application level context.
    public static UserPreferences getInstance(Context context) {
        if (userPreferences == null) {
            userPreferences = new UserPreferences(context.getApplicationContext());
        }
        return userPreferences;
    }


    public void setAuthInfo(SignInResponse signInResponse, boolean isCompleteResponse){
        SharedPreferences.Editor editor =  userInfo.edit()
                .putString(UserInformationKeys.token, "Bearer " + signInResponse.getIdToken())
                .putString(UserInformationKeys.refreshToken, signInResponse.getRefreshToken())
                .putLong(UserInformationKeys.expireDate, new Date().getTime() + signInResponse.getExpiresIn() * 1000);
        if (isCompleteResponse){
            editor
                    .putString(UserInformationKeys.email, signInResponse.getEmail())
                    .putString(UserInformationKeys.userName, signInResponse.getUserName())
                    .putString(UserInformationKeys.userId, signInResponse.getLocalId());
        }
        editor.apply();
    }

    public String getName(){
        return userInfo.getString(UserInformationKeys.userName, Utils.EMPTY_STRING);
    }

    public String getUserId(){
        return userInfo.getString(UserInformationKeys.userId, Utils.EMPTY_STRING);
    }
    public String getEmail(){
        return userInfo.getString(UserInformationKeys.email, Utils.EMPTY_STRING);
    }
    public boolean userIsAuthenticated(){
        return !Utils.stringIsNullOrEmpty(userInfo.getString(UserInformationKeys.token, Utils.EMPTY_STRING)) &&
                !Utils.stringIsNullOrEmpty(userInfo.getString(UserInformationKeys.refreshToken, Utils.EMPTY_STRING));
    }

    public long getLastSyncTime(){
        return appSettings.getLong(AppSettingsKeys.lastSelectedFragment, 0);
    }

    public void setLastSyncTime(long timeStamp){
        appSettings.edit().putLong(AppSettingsKeys.lastSyncTime, timeStamp).apply();
    }

    public void setLastSelectedFragment(int lastSelectedFragmentId){
        appSettings.edit().putInt(AppSettingsKeys.lastSelectedFragment, lastSelectedFragmentId).apply();
    }

    public int getLastSelectedFragment(){
        return appSettings.getInt(AppSettingsKeys.lastSelectedFragment, 1);
    }

    public void deleteAll(){
        appSettings.edit().clear().apply();
        userInfo.edit().clear().apply();
    }
    public String getAuthorizationToken(){
        return userInfo.getString(UserInformationKeys.token, Utils.EMPTY_STRING);
    }
    public boolean tokenHasExpired(){
        return new Date().getTime() > userInfo.getLong(UserInformationKeys.expireDate, 0);
    }
    public String getRefreshToken(){
        return userInfo.getString(UserInformationKeys.refreshToken, Utils.EMPTY_STRING);
    }
}
