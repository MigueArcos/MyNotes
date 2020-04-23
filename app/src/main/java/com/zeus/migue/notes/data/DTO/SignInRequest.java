package com.zeus.migue.notes.data.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;

public class SignInRequest extends JsonConverter {
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private String password;

    public SignInRequest() {
    }

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
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
