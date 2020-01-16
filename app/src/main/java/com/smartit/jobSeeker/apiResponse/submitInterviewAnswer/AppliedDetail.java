package com.smartit.jobSeeker.apiResponse.submitInterviewAnswer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedDetail {

    @SerializedName("applied_id")
    @Expose
    private String appliedId;

    @SerializedName("pitcher_id")
    @Expose
    private String pitcherId;

    @SerializedName("source")
    @Expose
    private String source;

    public String getAppliedId() {
        return appliedId;
    }

    public void setAppliedId(String appliedId) {
        this.appliedId = appliedId;
    }

    public String getPitcherId() {
        return pitcherId;
    }

    public void setPitcherId(String pitcherId) {
        this.pitcherId = pitcherId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}