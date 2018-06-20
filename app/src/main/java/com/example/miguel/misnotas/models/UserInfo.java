package com.example.miguel.misnotas.models;

import com.google.gson.Gson;

/**
 * Created by 79812 on 26/05/2018.
 */

public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String password;

    public UserInfo() {

    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
