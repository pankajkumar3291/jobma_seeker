package com.smartit.jobSeeker.atsApiResponses.ats_deeplink.deeplinking_url;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interview {

    @SerializedName("interviewId")
    @Expose
    private Integer interviewId;

    @SerializedName("kit_id")
    @Expose
    private Integer kitId;

    @SerializedName("job_title")
    @Expose
    private String jobTitle;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("catcher_id")
    @Expose
    private Integer catcherId;

    @SerializedName("date")
    @Expose
    private String date;

    public Integer getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(Integer interviewId) {
        this.interviewId = interviewId;
    }

    public Integer getKitId() {
        return kitId;
    }

    public void setKitId(Integer kitId) {
        this.kitId = kitId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCatcherId() {
        return catcherId;
    }

    public void setCatcherId(Integer catcherId) {
        this.catcherId = catcherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}