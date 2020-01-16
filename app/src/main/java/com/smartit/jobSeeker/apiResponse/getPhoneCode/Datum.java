package com.smartit.jobSeeker.apiResponse.getPhoneCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("country_name")
    @Expose
    private String countryName;

    @SerializedName("phone_code")
    @Expose
    private Integer phoneCode;

    @SerializedName("country_code")
    @Expose
    private String countryCode;


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Integer phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}