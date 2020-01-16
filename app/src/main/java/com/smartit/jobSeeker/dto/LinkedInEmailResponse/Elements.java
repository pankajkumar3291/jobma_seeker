package com.smartit.jobSeeker.dto.LinkedInEmailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Elements {
    @Expose
    @SerializedName("handle~")
    private Handle1 handle1;
    @Expose
    @SerializedName("handle")
    private String handle;

    public Handle1 getHandle1() {
        return handle1;
    }

    public void setHandle1(Handle1 handle1) {
        this.handle1 = handle1;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
