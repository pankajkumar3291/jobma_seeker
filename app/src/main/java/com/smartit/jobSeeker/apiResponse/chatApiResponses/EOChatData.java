package com.smartit.jobSeeker.apiResponse.chatApiResponses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EOChatData {

    @SerializedName("information")
    @Expose
    private String information;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}