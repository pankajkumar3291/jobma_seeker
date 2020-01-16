package com.smartit.jobSeeker.apiResponse.recordedVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordedVideo {

    @SerializedName("error")
    @Expose
    private Integer error;

    @SerializedName("path")
    @Expose
    private Path path;

    @SerializedName("message")
    @Expose
    private String message;


    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}