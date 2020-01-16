package com.smartit.jobSeeker.apiResponse.getProfileDetail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

    @SerializedName("functional")
    @Expose
    private List<Functional> functional = null;

    @SerializedName("industry")
    @Expose
    private List<Industry> industry = null;

    @SerializedName("pitcher_id")
    @Expose
    private Integer pitcherId;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    @SerializedName("state_id")
    @Expose
    private Integer stateId;

    @SerializedName("country_id")
    @Expose
    private Integer countryId;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("pay_rate")
    @Expose
    private String payRate;

    @SerializedName("ctc")
    @Expose
    private String ctc;

    @SerializedName("expected_ctc")
    @Expose
    private String expectedCtc;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("currency_type")
    @Expose
    private String currencyType;

    @SerializedName("current_location")
    @Expose
    private String currentLocation;

    @SerializedName("expected_location")
    @Expose
    private String expectedLocation;

    @SerializedName("relocate")
    @Expose
    private String relocate;

    @SerializedName("emp_status")
    @Expose
    private String empStatus;

    @SerializedName("desire_jobtype")
    @Expose
    private String desireJobtype;

    @SerializedName("contract")
    @Expose
    private String contract;

    public List<Functional> getFunctional() {
        return functional;
    }

    public void setFunctional(List<Functional> functional) {
        this.functional = functional;
    }

    public List<Industry> getIndustry() {
        return industry;
    }

    public void setIndustry(List<Industry> industry) {
        this.industry = industry;
    }

    public Integer getPitcherId() {
        return pitcherId;
    }

    public void setPitcherId(Integer pitcherId) {
        this.pitcherId = pitcherId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public String getCtc() {
        return ctc;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public String getExpectedCtc() {
        return expectedCtc;
    }

    public void setExpectedCtc(String expectedCtc) {
        this.expectedCtc = expectedCtc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getExpectedLocation() {
        return expectedLocation;
    }

    public void setExpectedLocation(String expectedLocation) {
        this.expectedLocation = expectedLocation;
    }

    public String getRelocate() {
        return relocate;
    }

    public void setRelocate(String relocate) {
        this.relocate = relocate;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getDesireJobtype() {
        return desireJobtype;
    }

    public void setDesireJobtype(String desireJobtype) {
        this.desireJobtype = desireJobtype;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

}
