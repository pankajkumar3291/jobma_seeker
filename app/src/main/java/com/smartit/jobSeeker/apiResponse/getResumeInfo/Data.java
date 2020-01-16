package com.smartit.jobSeeker.apiResponse.getResumeInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("resume_name")
    @Expose
    private String resumeName;

    @SerializedName("resume_url")
    @Expose
    private String resumeUrl;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("resume_show")
    @Expose
    private String resumeShow;

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getResumeShow() {
        return resumeShow;
    }

    public void setResumeShow(String resumeShow) {
        this.resumeShow = resumeShow;
    }

}