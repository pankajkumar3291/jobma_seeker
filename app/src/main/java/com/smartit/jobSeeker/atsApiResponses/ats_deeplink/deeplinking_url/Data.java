package com.smartit.jobSeeker.atsApiResponses.ats_deeplink.deeplinking_url;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("question")
    @Expose
    private List<Question> question = null;

    @SerializedName("interview")
    @Expose
    private Interview interview;

    @SerializedName("catcherPic")
    @Expose
    private String catcherPic;


    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public String getCatcherPic() {
        return catcherPic;
    }

    public void setCatcherPic(String catcherPic) {
        this.catcherPic = catcherPic;
    }

}
