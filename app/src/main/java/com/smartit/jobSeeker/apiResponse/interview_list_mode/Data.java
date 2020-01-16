package com.smartit.jobSeeker.apiResponse.interview_list_mode;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("interview_list")
    @Expose
    private List<InterviewList> interviewList = null;

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("remaining")
    @Expose
    private Integer remaining;


    public List<InterviewList> getInterviewList() {
        return interviewList;
    }

    public void setInterviewList(List<InterviewList> interviewList) {
        this.interviewList = interviewList;
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