package com.zeus.migue.notes.data.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpRequest extends SignInRequest {
    @SerializedName("Username")
    @Expose
    private String userName;

    public SignUpRequest(){

    }
    public SignUpRequest(String email, String userName, String password){
        super(email, password);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
