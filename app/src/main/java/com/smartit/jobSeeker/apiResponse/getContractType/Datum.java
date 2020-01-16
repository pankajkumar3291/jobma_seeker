package com.smartit.jobSeeker.apiResponse.getContractType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("key")
    @Expose
    private Integer key;

    @SerializedName("title")
    @Expose
    private String title;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}