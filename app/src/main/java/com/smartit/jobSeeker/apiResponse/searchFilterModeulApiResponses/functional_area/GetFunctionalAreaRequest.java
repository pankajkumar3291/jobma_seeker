package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFunctionalAreaRequest {


    @SerializedName("data")
    @Expose
    private List<FunctionalAreaData> data = null;
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;

    public List<FunctionalAreaData> getData() {
        return data;
    }

    public void setData(List<FunctionalAreaData> data) {
        this.data = data;
    }

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