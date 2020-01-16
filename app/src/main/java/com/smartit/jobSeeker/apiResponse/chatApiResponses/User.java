package com.smartit.jobSeeker.apiResponse.chatApiResponses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;
    @SerializedName("jobma_pitcher_fname")
    @Expose
    private String jobmaPitcherFname;

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public String getJobmaPitcherFname() {
        return jobmaPitcherFname;
    }

    public void setJobmaPitcherFname(String jobmaPitcherFname) {
        this.jobmaPitcherFname = jobmaPitcherFname;
    }

}