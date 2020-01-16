package com.smartit.jobSeeker.apiResponse.jobDescriptionNew;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobDetail {

    @SerializedName("industry")
    @Expose
    private List<Industry> industry = null;
    @SerializedName("functional")
    @Expose
    private List<Functional> functional = null;
    @SerializedName("jobma_job_post_id")
    @Expose
    private Integer jobmaJobPostId;
    @SerializedName("jobma_job_title")
    @Expose
    private String jobmaJobTitle;
    @SerializedName("jobma_job_description")
    @Expose
    private String jobmaJobDescription;
    @SerializedName("jobma_job_type")
    @Expose
    private String jobmaJobType;
    @SerializedName("jobma_job_min_salary")
    @Expose
    private String jobmaJobMinSalary;
    @SerializedName("jobma_job_max_salary")
    @Expose
    private String jobmaJobMaxSalary;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("jobma_job_expiry_date")
    @Expose
    private String jobmaJobExpiryDate;
    @SerializedName("jobma_job_min_exp")
    @Expose
    private String jobmaJobMinExp;
    @SerializedName("jobma_job_max_exp")
    @Expose
    private String jobmaJobMaxExp;
    @SerializedName("jobma_job_notice_period")
    @Expose
    private String jobmaJobNoticePeriod;
    @SerializedName("jobma_job_keywords")
    @Expose
    private String jobmaJobKeywords;
    @SerializedName("jobma_job_company_name")
    @Expose
    private String jobmaJobCompanyName;
    @SerializedName("jobma_job_locations")
    @Expose
    private String jobmaJobLocations;
    @SerializedName("jobma_job_company_profile")
    @Expose
    private String jobmaJobCompanyProfile;
    @SerializedName("jobma_job_recruiter_name")
    @Expose
    private String jobmaJobRecruiterName;
    @SerializedName("jobma_job_recruiter_email")
    @Expose
    private String jobmaJobRecruiterEmail;
    @SerializedName("jobma_job_recruiter_phone")
    @Expose
    private String jobmaJobRecruiterPhone;
    @SerializedName("is_private_job")
    @Expose
    private String isPrivateJob;
    @SerializedName("jobma_job_currency")
    @Expose
    private String jobmaJobCurrency;
    @SerializedName("jobma_job_salary_type")
    @Expose
    private String jobmaJobSalaryType;
    @SerializedName("jobma_catcher_photo")
    @Expose
    private String jobmaCatcherPhoto;
    @SerializedName("job_video")
    @Expose
    private String jobVideo;
    @SerializedName("job_rtsp")
    @Expose
    private String jobRtsp;
    @SerializedName("job_hls")
    @Expose
    private String jobHls;
    @SerializedName("job_dash")
    @Expose
    private String jobDash;
    @SerializedName("job_poster")
    @Expose
    private String jobPoster;
    @SerializedName("company_rtsp")
    @Expose
    private String companyRtsp;
    @SerializedName("company_hls")
    @Expose
    private String companyHls;
    @SerializedName("company_dash")
    @Expose
    private String companyDash;
    @SerializedName("company_video")
    @Expose
    private String companyVideo;
    @SerializedName("company_poster")
    @Expose
    private String companyPoster;
    @SerializedName("job-link")
    @Expose
    private String jobLink;

    public List<Industry> getIndustry() {
        return industry;
    }

    public void setIndustry(List<Industry> industry) {
        this.industry = industry;
    }

    public List<Functional> getFunctional() {
        return functional;
    }

    public void setFunctional(List<Functional> functional) {
        this.functional = functional;
    }

    public Integer getJobmaJobPostId() {
        return jobmaJobPostId;
    }

    public void setJobmaJobPostId(Integer jobmaJobPostId) {
        this.jobmaJobPostId = jobmaJobPostId;
    }

    public String getJobmaJobTitle() {
        return jobmaJobTitle;
    }

    public void setJobmaJobTitle(String jobmaJobTitle) {
        this.jobmaJobTitle = jobmaJobTitle;
    }

    public String getJobmaJobDescription() {
        return jobmaJobDescription;
    }

    public void setJobmaJobDescription(String jobmaJobDescription) {
        this.jobmaJobDescription = jobmaJobDescription;
    }

    public String getJobmaJobType() {
        return jobmaJobType;
    }

    public void setJobmaJobType(String jobmaJobType) {
        this.jobmaJobType = jobmaJobType;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getJobmaJobExpiryDate() {
        return jobmaJobExpiryDate;
    }

    public void setJobmaJobExpiryDate(String jobmaJobExpiryDate) {
        this.jobmaJobExpiryDate = jobmaJobExpiryDate;
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

    public String getJobmaJobNoticePeriod() {
        return jobmaJobNoticePeriod;
    }

    public void setJobmaJobNoticePeriod(String jobmaJobNoticePeriod) {
        this.jobmaJobNoticePeriod = jobmaJobNoticePeriod;
    }

    public String getJobmaJobKeywords() {
        return jobmaJobKeywords;
    }

    public void setJobmaJobKeywords(String jobmaJobKeywords) {
        this.jobmaJobKeywords = jobmaJobKeywords;
    }

    public String getJobmaJobCompanyName() {
        return jobmaJobCompanyName;
    }

    public void setJobmaJobCompanyName(String jobmaJobCompanyName) {
        this.jobmaJobCompanyName = jobmaJobCompanyName;
    }

    public String getJobmaJobLocations() {
        return jobmaJobLocations;
    }

    public void setJobmaJobLocations(String jobmaJobLocations) {
        this.jobmaJobLocations = jobmaJobLocations;
    }

    public String getJobmaJobCompanyProfile() {
        return jobmaJobCompanyProfile;
    }

    public void setJobmaJobCompanyProfile(String jobmaJobCompanyProfile) {
        this.jobmaJobCompanyProfile = jobmaJobCompanyProfile;
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

    public String getIsPrivateJob() {
        return isPrivateJob;
    }

    public void setIsPrivateJob(String isPrivateJob) {
        this.isPrivateJob = isPrivateJob;
    }

    public String getJobmaJobCurrency() {
        return jobmaJobCurrency;
    }

    public void setJobmaJobCurrency(String jobmaJobCurrency) {
        this.jobmaJobCurrency = jobmaJobCurrency;
    }

    public String getJobmaJobSalaryType() {
        return jobmaJobSalaryType;
    }

    public void setJobmaJobSalaryType(String jobmaJobSalaryType) {
        this.jobmaJobSalaryType = jobmaJobSalaryType;
    }

    public String getJobmaCatcherPhoto() {
        return jobmaCatcherPhoto;
    }

    public void setJobmaCatcherPhoto(String jobmaCatcherPhoto) {
        this.jobmaCatcherPhoto = jobmaCatcherPhoto;
    }

    public String getJobVideo() {
        return jobVideo;
    }

    public void setJobVideo(String jobVideo) {
        this.jobVideo = jobVideo;
    }

    public String getJobRtsp() {
        return jobRtsp;
    }

    public void setJobRtsp(String jobRtsp) {
        this.jobRtsp = jobRtsp;
    }

    public String getJobHls() {
        return jobHls;
    }

    public void setJobHls(String jobHls) {
        this.jobHls = jobHls;
    }

    public String getJobDash() {
        return jobDash;
    }

    public void setJobDash(String jobDash) {
        this.jobDash = jobDash;
    }

    public String getJobPoster() {
        return jobPoster;
    }

    public void setJobPoster(String jobPoster) {
        this.jobPoster = jobPoster;
    }

    public String getCompanyRtsp() {
        return companyRtsp;
    }

    public void setCompanyRtsp(String companyRtsp) {
        this.companyRtsp = companyRtsp;
    }

    public String getCompanyHls() {
        return companyHls;
    }

    public void setCompanyHls(String companyHls) {
        this.companyHls = companyHls;
    }

    public String getCompanyDash() {
        return companyDash;
    }

    public void setCompanyDash(String companyDash) {
        this.companyDash = companyDash;
    }

    public String getCompanyVideo() {
        return companyVideo;
    }

    public void setCompanyVideo(String companyVideo) {
        this.companyVideo = companyVideo;
    }

    public String getCompanyPoster() {
        return companyPoster;
    }

    public void setCompanyPoster(String companyPoster) {
        this.companyPoster = companyPoster;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

}