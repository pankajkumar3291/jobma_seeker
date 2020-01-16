package com.smartit.jobSeeker.apiResponse.inviationModule;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssessmentQuestionSet {

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