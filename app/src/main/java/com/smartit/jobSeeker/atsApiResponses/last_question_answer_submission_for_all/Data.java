package com.smartit.jobSeeker.atsApiResponses.last_question_answer_submission_for_all;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("applied_detail")
    @Expose
    private AppliedDetail appliedDetail;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AppliedDetail getAppliedDetail() {
        return appliedDetail;
    }

    public void setAppliedDetail(AppliedDetail appliedDetail) {
        this.appliedDetail = appliedDetail;
    }

}