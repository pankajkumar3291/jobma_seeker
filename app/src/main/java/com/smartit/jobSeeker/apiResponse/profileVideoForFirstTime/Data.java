package com.smartit.jobSeeker.apiResponse.profileVideoForFirstTime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("fileurl")
    @Expose
    private String fileurl;

    @SerializedName("rtsp")
    @Expose
    private String rtsp;

    @SerializedName("hls")
    @Expose
    private String hls;


    public String getDash() {
        return dash;
    }

    public void setDash(String dash) {
        this.dash = dash;
    }

    @SerializedName("dash")
    @Expose
    private String dash;




    @SerializedName("poster")
    @Expose
    private String poster;


    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
    }

    public String getHls() {
        return hls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

}