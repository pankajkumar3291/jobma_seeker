package com.smartit.jobSeeker.atsApiResponses.submit_answer_video_for_ats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoSubmitAnsForAts {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}