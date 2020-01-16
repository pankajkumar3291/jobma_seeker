package com.smartit.jobSeeker.dto.LinkedInNameResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreferredLocale {
    @Expose
    @SerializedName("language")
    private String language;
    @Expose
    @SerializedName("country")
    private String country;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
