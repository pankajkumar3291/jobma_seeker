package com.smartit.jobSeeker.dto.LinkedInNameResponse;

public class NameResponse {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("lastName")
    private LastName lastName;

    public LastName getLastName() {
        return lastName;
    }

    public String getLocalizedLastName() {
        return localizedLastName;
    }

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("localizedLastName")
    private String localizedLastName;
}
