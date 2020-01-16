package com.smartit.jobSeeker.apiResponse.getAudioResume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("audio_resume")
    @Expose
    private String audioResume;

    @SerializedName("audio_type")
    @Expose
    private String audioType;

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

    public String getAudioResume() {
        return audioResume;
    }

    public void setAudioResume(String audioResume) {
        this.audioResume = audioResume;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

}
