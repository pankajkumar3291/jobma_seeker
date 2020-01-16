package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.job_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchJobData implements Serializable {

    @SerializedName("job_assessment_kit_id")
    @Expose
    private Integer jobAssessmentKitId;
    @SerializedName("job_post_id")
    @Expose
    private Integer jobPostId;
    @SerializedName("hot_job")
    @Expose
    private Integer hotJob;
    @SerializedName("paper_ids")
    @Expose
    private String paperIds;
    @SerializedName("jobma_job_title")
    @Expose
    private String jobmaJobTitle;
    @SerializedName("jobma_catcher_id")
    @Expose
    private Integer jobmaCatcherId;
    @SerializedName("jobma_job_description")
    @Expose
    private String jobmaJobDescription;
    @SerializedName("jobma_catcher_company")
    @Expose
    private String jobmaCatcherCompany;
    @SerializedName("jobma_job_type")
    @Expose
    private String jobmaJobType;
    @SerializedName("jobma_job_min_exp")
    @Expose
    private String jobmaJobMinExp;
    @SerializedName("jobma_job_max_exp")
    @Expose
    private String jobmaJobMaxExp;
    @SerializedName("jobma_job_min_salary")
    @Expose
    private String jobmaJobMinSalary;
    @SerializedName("jobma_job_max_salary")
    @Expose
    private String jobmaJobMaxSalary;
    @SerializedName("jobma_job_notice_period")
    @Expose
    private String jobmaJobNoticePeriod;
    @SerializedName("jobma_job_locations")
    @Expose
    private String jobmaJobLocations;
    @SerializedName("jobma_job_interview_questions_ids")
    @Expose
    private String jobmaJobInterviewQuestionsIds;
    @SerializedName("interview_que_duration")
    @Expose
    private String interviewQueDuration;
    @SerializedName("jobma_job_keywords")
    @Expose
    private String jobmaJobKeywords;
    @SerializedName("jobma_job_recruiter_name")
    @Expose
    private String jobmaJobRecruiterName;
    @SerializedName("jobma_job_recruiter_email")
    @Expose
    private String jobmaJobRecruiterEmail;
    @SerializedName("jobma_job_recruiter_phone")
    @Expose
    private String jobmaJobRecruiterPhone;
    @SerializedName("jobma_job_recruiter_ext")
    @Expose
    private String jobmaJobRecruiterExt;
    @SerializedName("jobma_job_currency")
    @Expose
    private Integer jobmaJobCurrency;
    @SerializedName("jobma_job_salary_type")
    @Expose
    private String jobmaJobSalaryType;
    @SerializedName("jobma_job_company_name")
    @Expose
    private String jobmaJobCompanyName;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("jobma_job_expiry_date")
    @Expose
    private String jobmaJobExpiryDate;
    @SerializedName("jobma_job_city")
    @Expose
    private String jobmaJobCity;
    @SerializedName("jobma_job_country")
    @Expose
    private String jobmaJobCountry;
    @SerializedName("jobma_job_address")
    @Expose
    private String jobmaJobAddress;
    @SerializedName("jobma_job_state")
    @Expose
    private String jobmaJobState;
    @SerializedName("jobma_catcher_photo")
    @Expose
    private String jobmaCatcherPhoto;
    @SerializedName("industry")
    @Expose
    private String industry;
    @SerializedName("functional_area")
    @Expose
    private String functionalArea;
    @SerializedName("apply_status")
    @Expose
    private Integer applyStatus;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getJobAssessmentKitId() {
        return jobAssessmentKitId;
    }

    public void setJobAssessmentKitId(Integer jobAssessmentKitId) {
        this.jobAssessmentKitId = jobAssessmentKitId;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public Integer getHotJob() {
        return hotJob;
    }

    public void setHotJob(Integer hotJob) {
        this.hotJob = hotJob;
    }

    public String getPaperIds() {
        return paperIds;
    }

    public void setPaperIds(String paperIds) {
        this.paperIds = paperIds;
    }

    public String getJobmaJobTitle() {
        return jobmaJobTitle;
    }

    public void setJobmaJobTitle(String jobmaJobTitle) {
        this.jobmaJobTitle = jobmaJobTitle;
    }

    public Integer getJobmaCatcherId() {
        return jobmaCatcherId;
    }

    public void setJobmaCatcherId(Integer jobmaCatcherId) {
        this.jobmaCatcherId = jobmaCatcherId;
    }

    public String getJobmaJobDescription() {
        return jobmaJobDescription;
    }

    public void setJobmaJobDescription(String jobmaJobDescription) {
        this.jobmaJobDescription = jobmaJobDescription;
    }

    public String getJobmaCatcherCompany() {
        return jobmaCatcherCompany;
    }

    public void setJobmaCatcherCompany(String jobmaCatcherCompany) {
        this.jobmaCatcherCompany = jobmaCatcherCompany;
    }

    public String getJobmaJobType() {
        return jobmaJobType;
    }

    public void setJobmaJobType(String jobmaJobType) {
        this.jobmaJobType = jobmaJobType;
    }

    public String getJobmaJobMinExp() {
        return jobmaJobMinExp;
    }

    public void setJobmaJobMinExp(String jobmaJobMinExp) {
        this.jobmaJobMinExp = jobmaJobMinExp;
    }

    public String getJobmaJobMaxExp() {
        return jobmaJobMaxExp;
    }

    public void setJobmaJobMaxExp(String jobmaJobMaxExp) {
        this.jobmaJobMaxExp = jobmaJobMaxExp;
    }

    public String getJobmaJobMinSalary() {
        return jobmaJobMinSalary;
    }

    public void setJobmaJobMinSalary(String jobmaJobMinSalary) {
        this.jobmaJobMinSalary = jobmaJobMinSalary;
    }

    public String getJobmaJobMaxSalary() {
        return jobmaJobMaxSalary;
    }

    public void setJobmaJobMaxSalary(String jobmaJobMaxSalary) {
        this.jobmaJobMaxSalary = jobmaJobMaxSalary;
    }

    public String getJobmaJobNoticePeriod() {
        return jobmaJobNoticePeriod;
    }

    public void setJobmaJobNoticePeriod(String jobmaJobNoticePeriod) {
        this.jobmaJobNoticePeriod = jobmaJobNoticePeriod;
    }

    public String getJobmaJobLocations() {
        return jobmaJobLocations;
    }

    public void setJobmaJobLocations(String jobmaJobLocations) {
        this.jobmaJobLocations = jobmaJobLocations;
    }

    public String getJobmaJobInterviewQuestionsIds() {
        return jobmaJobInterviewQuestionsIds;
    }

    public void setJobmaJobInterviewQuestionsIds(String jobmaJobInterviewQuestionsIds) {
        this.jobmaJobInterviewQuestionsIds = jobmaJobInterviewQuestionsIds;
    }

    public String getInterviewQueDuration() {
        return interviewQueDuration;
    }

    public void setInterviewQueDuration(String interviewQueDuration) {
        this.interviewQueDuration = interviewQueDuration;
    }

    public String getJobmaJobKeywords() {
        return jobmaJobKeywords;
    }

    public void setJobmaJobKeywords(String jobmaJobKeywords) {
        this.jobmaJobKeywords = jobmaJobKeywords;
    }

    public String getJobmaJobRecruiterName() {
        return jobmaJobRecruiterName;
    }

    public void setJobmaJobRecruiterName(String jobmaJobRecruiterName) {
        this.jobmaJobRecruiterName = jobmaJobRecruiterName;
    }

    public String getJobmaJobRecruiterEmail() {
        return jobmaJobRecruiterEmail;
    }

    public void setJobmaJobRecruiterEmail(String jobmaJobRecruiterEmail) {
        this.jobmaJobRecruiterEmail = jobmaJobRecruiterEmail;
    }

    public String getJobmaJobRecruiterPhone() {
        return jobmaJobRecruiterPhone;
    }

    public void setJobmaJobRecruiterPhone(String jobmaJobRecruiterPhone) {
        this.jobmaJobRecruiterPhone = jobmaJobRecruiterPhone;
    }

    public String getJobmaJobRecruiterExt() {
        return jobmaJobRecruiterExt;
    }

    public void setJobmaJobRecruiterExt(String jobmaJobRecruiterExt) {
        this.jobmaJobRecruiterExt = jobmaJobRecruiterExt;
    }

    public Integer getJobmaJobCurrency() {
        return jobmaJobCurrency;
    }

    public void setJobmaJobCurrency(Integer jobmaJobCurrency) {
        this.jobmaJobCurrency = jobmaJobCurrency;
    }

    public String getJobmaJobSalaryType() {
        return jobmaJobSalaryType;
    }

    public void setJobmaJobSalaryType(String jobmaJobSalaryType) {
        this.jobmaJobSalaryType = jobmaJobSalaryType;
    }

    public String getJobmaJobCompanyName() {
        return jobmaJobCompanyName;
    }

    public void setJobmaJobCompanyName(String jobmaJobCompanyName) {
        this.jobmaJobCompanyName = jobmaJobCompanyName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(Integer lon) {
        this.lon = lon;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getJobmaJobExpiryDate() {
        return jobmaJobExpiryDate;
    }

    public void setJobmaJobExpiryDate(String jobmaJobExpiryDate) {
        this.jobmaJobExpiryDate = jobmaJobExpiryDate;
    }

    public String getJobmaJobCity() {
        return jobmaJobCity;
    }

    public void setJobmaJobCity(String jobmaJobCity) {
        this.jobmaJobCity = jobmaJobCity;
    }

    public String getJobmaJobCountry() {
        return jobmaJobCountry;
    }

    public void setJobmaJobCountry(String jobmaJobCountry) {
        this.jobmaJobCountry = jobmaJobCountry;
    }

    public String getJobmaJobAddress() {
        return jobmaJobAddress;
    }

    public void setJobmaJobAddress(String jobmaJobAddress) {
        this.jobmaJobAddress = jobmaJobAddress;
    }

    public String getJobmaJobState() {
        return jobmaJobState;
    }

    public void setJobmaJobState(String jobmaJobState) {
        this.jobmaJobState = jobmaJobState;
    }

    public String getJobmaCatcherPhoto() {
        return jobmaCatcherPhoto;
    }

    public void setJobmaCatcherPhoto(String jobmaCatcherPhoto) {
        this.jobmaCatcherPhoto = jobmaCatcherPhoto;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}