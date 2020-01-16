package com.smartit.jobSeeker.apiResponse.inviationModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitationList {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    @SerializedName("invitation_date")
    @Expose
    private String invitationDate;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("mode")
    @Expose
    private String mode;

    @SerializedName("acceptation")
    @Expose
    private String acceptation;

    @SerializedName("interview_status")
    @Expose
    private String interviewStatus;

    @SerializedName("invitation_expiry")
    @Expose
    private String invitationExpiry;

    @SerializedName("assessment_question_set")
    @Expose
    private AssessmentQuestionSet assessmentQuestionSet;

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

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(String acceptation) {
        this.acceptation = acceptation;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public String getInvitationExpiry() {
        return invitationExpiry;
    }

    public void setInvitationExpiry(String invitationExpiry) {
        this.invitationExpiry = invitationExpiry;
    }

    public AssessmentQuestionSet getAssessmentQuestionSet() {
        return assessmentQuestionSet;
    }

    public void setAssessmentQuestionSet(AssessmentQuestionSet assessmentQuestionSet) {
        this.assessmentQuestionSet = assessmentQuestionSet;
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