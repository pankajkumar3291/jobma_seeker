package com.smartit.jobSeeker.apiResponse.applicationHistory.applyFilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedList {

    @SerializedName("post_id")
    @Expose
    private Integer postId;

    @SerializedName("posteddate")
    @Expose
    private String posteddate;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("companyname")
    @Expose
    private String companyname;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("appliedMode")
    @Expose
    private String appliedMode;

    @SerializedName("applieddate")
    @Expose
    private String applieddate;

    @SerializedName("apply_mode")
    @Expose
    private String applyMode;


    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(String posteddate) {
        this.posteddate = posteddate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedMode() {
        return appliedMode;
    }

    public void setAppliedMode(String appliedMode) {
        this.appliedMode = appliedMode;
    }

    public String getApplieddate() {
        return applieddate;
    }

    public void setApplieddate(String applieddate) {
        this.applieddate = applieddate;
    }

    public String getApplyMode() {
        return applyMode;
    }

    public void setApplyMode(String applyMode) {
        this.applyMode = applyMode;
    }

}