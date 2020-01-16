package com.smartit.jobSeeker.apiResponse.qrCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("jobma_pitcher_id")
    @Expose
    private Integer jobmaPitcherId;

    @SerializedName("jobma_pitcher_qrcode")
    @Expose
    private String jobmaPitcherQrcode;

    @SerializedName("qrcode")
    @Expose
    private String qrcode;

    public Integer getJobmaPitcherId() {
        return jobmaPitcherId;
    }

    public void setJobmaPitcherId(Integer jobmaPitcherId) {
        this.jobmaPitcherId = jobmaPitcherId;
    }

    public String getJobmaPitcherQrcode() {
        return jobmaPitcherQrcode;
    }

    public void setJobmaPitcherQrcode(String jobmaPitcherQrcode) {
        this.jobmaPitcherQrcode = jobmaPitcherQrcode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

}
