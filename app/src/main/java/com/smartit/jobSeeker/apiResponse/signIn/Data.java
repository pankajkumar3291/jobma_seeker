package com.smartit.jobSeeker.apiResponse.signIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("first_time")
    @Expose
    private Integer firstTime;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("keyskills")
    @Expose
    private Integer keyskills;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Integer getKeyskills() {
        return keyskills;
    }

    public void setKeyskills(Integer keyskills) {
        this.keyskills = keyskills;
    }

}