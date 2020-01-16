package com.smartit.jobSeeker.atsApiResponses.ats_total_question;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("invitation_id")
    @Expose
    private String invitationId;
    @SerializedName("kit_id")
    @Expose
    private String kitId;
    @SerializedName("total_qustion")
    @Expose
    private Integer totalQustion;
    @SerializedName("remain_qustion")
    @Expose
    private Integer remainQustion;
    @SerializedName("total_duration")
    @Expose
    private String totalDuration;
    @SerializedName("total_video")
    @Expose
    private String totalVideo;
    @SerializedName("total_mcq")
    @Expose
    private String totalMcq;
    @SerializedName("total_essay")
    @Expose
    private String totalEssay;
    @SerializedName("video_duration")
    @Expose
    private String videoDuration;
    @SerializedName("mcq_duration")
    @Expose
    private String mcqDuration;
    @SerializedName("essay_duration")
    @Expose
    private String essayDuration;
    @SerializedName("total_question")
    @Expose
    private TotalQuestion totalQuestion;

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getKitId() {
        return kitId;
    }

    public void setKitId(String kitId) {
        this.kitId = kitId;
    }

    public Integer getTotalQustion() {
        return totalQustion;
    }

    public void setTotalQustion(Integer totalQustion) {
        this.totalQustion = totalQustion;
    }

    public Integer getRemainQustion() {
        return remainQustion;
    }

    public void setRemainQustion(Integer remainQustion) {
        this.remainQustion = remainQustion;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getTotalVideo() {
        return totalVideo;
    }

    public void setTotalVideo(String totalVideo) {
        this.totalVideo = totalVideo;
    }

    public String getTotalMcq() {
        return totalMcq;
    }

    public void setTotalMcq(String totalMcq) {
        this.totalMcq = totalMcq;
    }

    public String getTotalEssay() {
        return totalEssay;
    }

    public void setTotalEssay(String totalEssay) {
        this.totalEssay = totalEssay;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getMcqDuration() {
        return mcqDuration;
    }

    public void setMcqDuration(String mcqDuration) {
        this.mcqDuration = mcqDuration;
    }

    public String getEssayDuration() {
        return essayDuration;
    }

    public void setEssayDuration(String essayDuration) {
        this.essayDuration = essayDuration;
    }

    public TotalQuestion getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(TotalQuestion totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

}