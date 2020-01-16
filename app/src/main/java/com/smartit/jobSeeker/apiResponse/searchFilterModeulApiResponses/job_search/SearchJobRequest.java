package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchJobRequest implements Serializable {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("data")
    @Expose
    private List<SearchJobData> data = null;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public List<SearchJobData> getData() {
        return data;
    }

    public void setData(List<SearchJobData> data) {
        this.data = data;
    }

}
