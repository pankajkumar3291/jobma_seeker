package com.smartit.jobSeeker.apiResponse.jobDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("job_detail")
    @Expose
    private JobDetail jobDetail;

    @SerializedName("invitation_detail")
    @Expose
    private InvitationDetail invitationDetail;


    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public InvitationDetail getInvitationDetail() {
        return invitationDetail;
    }

    public void setInvitationDetail(InvitationDetail invitationDetail) {
        this.invitationDetail = invitationDetail;
    }

}