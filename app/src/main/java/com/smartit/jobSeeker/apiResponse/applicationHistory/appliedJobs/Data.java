package com.smartit.jobSeeker.apiResponse.applicationHistory.appliedJobs;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("applied_list")
    @Expose
    private List<AppliedList> appliedList = null;

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("remaining")
    @Expose
    private Integer remaining;


    public List<AppliedList> getAppliedList() {
        return appliedList;
    }

    public void setAppliedList(List<AppliedList> appliedList) {
        this.appliedList = appliedList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

}