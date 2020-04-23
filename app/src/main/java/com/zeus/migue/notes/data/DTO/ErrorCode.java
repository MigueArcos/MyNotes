package com.zeus.migue.notes.data.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zeus.migue.notes.infrastructure.contracts.JsonConverter;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public class ErrorCode extends JsonConverter {
    @SerializedName("StatusCode")
    @Expose
    private int statusCode;
    @SerializedName("Message")
    @Expose
    private String message = "Unknown error";
    private boolean IsUnknownError;

    public ErrorCode() {
    }

    public ErrorCode(int statusCode, String message, boolean isUnknownError) {
        this.statusCode = statusCode;
        this.message = message;
        IsUnknownError = isUnknownError;

        if (this.statusCode == 4000) {
            IsUnknownError = true;
        }
        if (Utils.stringIsNullOrEmpty(this.message)) {
            if (this.statusCode == 0) {
                this.statusCode = 4000;
                IsUnknownError = true;
            }
            this.message = "Error desconocido";
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        if (statusCode == 4000) {
            IsUnknownError = true;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (Utils.stringIsNullOrEmpty(this.message)) {
            if (statusCode == 0) {
                statusCode = 4000;
                IsUnknownError = true;
            }
            this.message = "Error desconocido";
        }
    }
    public Event toEvent(int messageType){
        return new Event(message, messageType);
    }
    public boolean isUnknownError() {
        return IsUnknownError;
    }
}

