package com.smartit.jobSeeker.apiResponse.displayEditJob;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("industry")
    @Expose
    private List<Industry> industry = null;

    @SerializedName("job_alert_id")
    @Expose
    private Integer jobAlertId;

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("skills")
    @Expose
    private String skills;

    @SerializedName("minexp")
    @Expose
    private Integer minexp;

    @SerializedName("maxexp")
    @Expose
    private Integer maxexp;

    @SerializedName("jobcurrency")
    @Expose
    private String jobcurrency;

    @SerializedName("minsalary")
    @Expose
    private Integer minsalary;

    @SerializedName("maxsalary")
    @Expose
    private Integer maxsalary;

    @SerializedName("salary_type")
    @Expose
    private String salaryType;

    @SerializedName("notification")
    @Expose
    private String notification;

    @SerializedName("addeddate")
    @Expose
    private String addeddate;

    @SerializedName("currency_type")
    @Expose
    private String currencyType;


    public List<Industry> getIndustry() {
        return industry;
    }

    public void setIndustry(List<Industry> industry) {
        this.industry = industry;
    }

    public Integer getJobAlertId() {
        return jobAlertId;
    }

    public void setJobAlertId(Integer jobAlertId) {
        this.jobAlertId = jobAlertId;
    }

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Integer getMinexp() {
        return minexp;
    }

    public void setMinexp(Integer minexp) {
        this.minexp = minexp;
    }

    public Integer getMaxexp() {
        return maxexp;
    }

    public void setMaxexp(Integer maxexp) {
        this.maxexp = maxexp;
    }

    public String getJobcurrency() {
        return jobcurrency;
    }

    public void setJobcurrency(String jobcurrency) {
        this.jobcurrency = jobcurrency;
    }

    public Integer getMinsalary() {
        return minsalary;
    }

    public void setMinsalary(Integer minsalary) {
        this.minsalary = minsalary;
    }

    public Integer getMaxsalary() {
        return maxsalary;
    }

    public void setMaxsalary(Integer maxsalary) {
        this.maxsalary = maxsalary;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(String addeddate) {
        this.addeddate = addeddate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

}