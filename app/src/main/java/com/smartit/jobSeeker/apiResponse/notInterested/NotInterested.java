package com.smartit.jobSeeker.apiResponse.notInterested;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotInterested {

    @SerializedName("error")
    @Expose
    private Integer error;


    @SerializedName("message")
    @Expose
    private String message;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}