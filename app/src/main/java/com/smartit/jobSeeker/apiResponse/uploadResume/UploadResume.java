package com.smartit.jobSeeker.apiResponse.uploadResume;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResume {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}