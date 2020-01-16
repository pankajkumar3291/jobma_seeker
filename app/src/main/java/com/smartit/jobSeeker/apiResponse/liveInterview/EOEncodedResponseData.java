package com.smartit.jobSeeker.apiResponse.liveInterview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EOEncodedResponseData implements Serializable {

    @SerializedName("pitcherId")
    @Expose
    private Integer pitcherId;
    @SerializedName("pitcherEmail")
    @Expose
    private String pitcherEmail;
    @SerializedName("pitcherPhoto")
    @Expose
    private String pitcherPhoto;
    @SerializedName("catcherEmail")
    @Expose
    private String catcherEmail;
    @SerializedName("catcherName")
    @Expose
    private String catcherName;
    @SerializedName("catcherPhoto")
    @Expose
    private String catcherPhoto;
    @SerializedName("jobid")
    @Expose
    private Integer jobid;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("inviteId")
    @Expose
    private Integer inviteId;
    @SerializedName("configTimeZone")
    @Expose
    private String configTimeZone;

    public Integer getPitcherId() {
        return pitcherId;
    }

    public void setPitcherId(Integer pitcherId) {
        this.pitcherId = pitcherId;
    }

    public String getPitcherEmail() {
        return pitcherEmail;
    }

    public void setPitcherEmail(String pitcherEmail) {
        this.pitcherEmail = pitcherEmail;
    }

    public String getPitcherPhoto() {
        return pitcherPhoto;
    }

    public void setPitcherPhoto(String pitcherPhoto) {
        this.pitcherPhoto = pitcherPhoto;
    }

    public String getCatcherEmail() {
        return catcherEmail;
    }

    public void setCatcherEmail(String catcherEmail) {
        this.catcherEmail = catcherEmail;
    }

    public String getCatcherName() {
        return catcherName;
    }

    public void setCatcherName(String catcherName) {
        this.catcherName = catcherName;
    }

    public String getCatcherPhoto() {
        return catcherPhoto;
    }

    public void setCatcherPhoto(String catcherPhoto) {
        this.catcherPhoto = catcherPhoto;
    }

    public Integer getJobid() {
        return jobid;
    }

    public void setJobid(Integer jobid) {
        this.jobid = jobid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    public String getConfigTimeZone() {
        return configTimeZone;
    }

    public void setConfigTimeZone(String configTimeZone) {
        this.configTimeZone = configTimeZone;
    }


}
