package com.smartit.jobSeeker.apiResponse.recordedVideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Path {

    @SerializedName("uploaded_file_url")
    @Expose
    private String uploadedFileUrl;

    @SerializedName("poster")
    @Expose
    private String poster;


    public String getUploadedFileUrl() {
        return uploadedFileUrl;
    }

    public void setUploadedFileUrl(String uploadedFileUrl) {
        this.uploadedFileUrl = uploadedFileUrl;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}