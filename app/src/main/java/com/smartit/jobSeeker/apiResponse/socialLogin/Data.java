package com.smartit.jobSeeker.apiResponse.socialLogin;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("first_time")
    @Expose
    private Integer firstTime;

    @SerializedName("token")
    @Expose
    private String token;


    public Integer getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Integer firstTime) {
        this.firstTime = firstTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}