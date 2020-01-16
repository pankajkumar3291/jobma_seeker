package com.smartit.jobSeeker.apiResponse.handShakeForDashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("phonecode")
    @Expose
    private String phonecode;

    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    @SerializedName("state_id")
    @Expose
    private Integer stateId;

    @SerializedName("country_id")
    @Expose
    private Integer countryId;

    @SerializedName("zip")
    @Expose
    private String zip;

    @SerializedName("imageurl")
    @Expose
    private String imageurl;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("comp")
    @Expose
    private String comp;

    @SerializedName("jobtitle")
    @Expose
    private String jobtitle;


    @SerializedName("exp")
    @Expose
    private Integer exp;


    // Shahzeb added this

    public Integer getExpmm() {
        return expmm;
    }

    public void setExpmm(Integer expmm) {
        this.expmm = expmm;
    }

    @SerializedName("expmm")
    @Expose
    private Integer expmm;


    // ended


    @SerializedName("keyskills")
    @Expose
    private String keyskills;

    @SerializedName("first_time")
    @Expose
    private Integer firstTime;

    @SerializedName("views")
    @Expose
    private Integer views;

    @SerializedName("applied")
    @Expose
    private Integer applied;

    @SerializedName("downloads")
    @Expose
    private Integer downloads;

    @SerializedName("pmeter")
    @Expose
    private Integer pmeter;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getKeyskills() {
        return keyskills;
    }

    public void setKeyskills(String keyskills) {
        this.keyskills = keyskills;
    }

    public Integer getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Integer firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getApplied() {
        return applied;
    }

    public void setApplied(Integer applied) {
        this.applied = applied;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public Integer getPmeter() {
        return pmeter;
    }

    public void setPmeter(Integer pmeter) {
        this.pmeter = pmeter;
    }

}