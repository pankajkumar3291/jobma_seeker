package com.smartit.jobSeeker.apiResponse.interview_list_mode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterviewList {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("invitation_date")
    @Expose
    private String invitationDate;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("jobma_interview_mode")
    @Expose
    private String jobmaInterviewMode;

    @SerializedName("jobma_interview_token")
    @Expose
    private String jobmaInterviewToken;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @SerializedName("company_name")
    @Expose
    private String companyName;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("interview_mode")
    @Expose
    private String interviewMode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public String getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(String invitationDate) {
        this.invitationDate = invitationDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJobmaInterviewMode() {
        return jobmaInterviewMode;
    }

    public void setJobmaInterviewMode(String jobmaInterviewMode) {
        this.jobmaInterviewMode = jobmaInterviewMode;
    }

    public String getJobmaInterviewToken() {
        return jobmaInterviewToken;
    }

    public void setJobmaInterviewToken(String jobmaInterviewToken) {
        this.jobmaInterviewToken = jobmaInterviewToken;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(String interviewMode) {
        this.interviewMode = interviewMode;
    }

}