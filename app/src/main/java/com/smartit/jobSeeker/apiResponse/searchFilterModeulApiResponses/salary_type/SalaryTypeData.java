package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.salary_type;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalaryTypeData {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("title")
    @Expose
    private String title;

    public SalaryTypeData(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString(){
        return title;
    }
}

