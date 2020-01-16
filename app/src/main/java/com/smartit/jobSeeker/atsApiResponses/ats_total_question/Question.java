package com.smartit.jobSeeker.atsApiResponses.ats_total_question;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Serializable {

    @SerializedName("ques")
    @Expose
    private Integer ques;

    @SerializedName("ques_title")
    @Expose
    private String quesTitle;

    @SerializedName("qtype")
    @Expose
    private String qtype;

    @SerializedName("thinktime")
    @Expose
    private String thinktime;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("attempts")
    @Expose
    private Integer attempts;

    @SerializedName("correct")
    @Expose
    private String correct;

    @SerializedName("optional")
    @Expose
    private String optional;

    @SerializedName("options")
    @Expose
    private List<Object> options = null;

    public Integer getQues() {
        return ques;
    }

    public void setQues(Integer ques) {
        this.ques = ques;
    }

    public String getQuesTitle() {
        return quesTitle;
    }

    public void setQuesTitle(String quesTitle) {
        this.quesTitle = quesTitle;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getThinktime() {
        return thinktime;
    }

    public void setThinktime(String thinktime) {
        this.thinktime = thinktime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }


    // IT HAS BEEN ADDED

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    @SerializedName("file_name")
    @Expose
    private String file_name;
    // END


    // added hls
    public String getHls() {
        return hls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    @SerializedName("hls")
    @Expose
    private String hls;
    // end hls


    // IT HAS BEEN ADDED


    public Integer getFile_status() {
        return file_status;
    }

    public void setFile_status(Integer file_status) {
        this.file_status = file_status;
    }

    @SerializedName("file_status")
    @Expose
    private Integer file_status;


    // END


    // added
    public String getFile_thumb() {
        return file_thumb;
    }

    public void setFile_thumb(String file_thumb) {
        this.file_thumb = file_thumb;
    }

    @SerializedName("file_thumb")
    @Expose
    private String file_thumb;

    // end


}
