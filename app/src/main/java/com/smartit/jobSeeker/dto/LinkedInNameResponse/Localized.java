package com.smartit.jobSeeker.dto.LinkedInNameResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Localized {
    @Expose
    @SerializedName("es_ES")
    private String es_ES;

    public String getEs_ES() {
        return es_ES;
    }

    public void setEs_ES(String es_ES) {
        this.es_ES = es_ES;
    }
}
