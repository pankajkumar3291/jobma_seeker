package com.smartit.jobSeeker.dto.LinkedInEmailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class LinkedInEmailResponse {
    @Expose
    @SerializedName("elements")
    List<Elements> elements;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }
}
