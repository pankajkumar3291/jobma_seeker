package com.smartit.jobSeeker.apiResponse.preRecordedInterviewKit;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalQuestion implements Serializable {

    @SerializedName("question")
    @Expose
    private List<Question> question = null;

    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

}