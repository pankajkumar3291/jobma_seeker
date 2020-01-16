package com.smartit.jobSeeker.atsApiResponses.ats_deeplink.deeplinking_url;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("thumb")
    @Expose
    private String thumb;

    @SerializedName("retake")
    @Expose
    private String retake;

    @SerializedName("duration")
    @Expose
    private Integer duration;

    @SerializedName("think_time")
    @Expose
    private String thinkTime;

    @SerializedName("ques_id")
    @Expose
    private Integer quesId;

    @SerializedName("ques_title")
    @Expose
    private String quesTitle;


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getRetake() {
        return retake;
    }

    public void setRetake(String retake) {
        this.retake = retake;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getThinkTime() {
        return thinkTime;
    }

    public void setThinkTime(String thinkTime) {
        this.thinkTime = thinkTime;
    }

    public Integer getQuesId() {
        return quesId;
    }

    public void setQuesId(Integer quesId) {
        this.quesId = quesId;
    }

    public String getQuesTitle() {
        return quesTitle;
    }

    public void setQuesTitle(String quesTitle) {
        this.quesTitle = quesTitle;
    }

}