package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("key")
    @Expose
    private Integer key;
    @SerializedName("title")
    @Expose
    private String title;

    public Datum(Integer key, String title) {
        this.key = key;
        this.title = title;
    }

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
    @Override
    public String toString() {
        return title;
    }
}