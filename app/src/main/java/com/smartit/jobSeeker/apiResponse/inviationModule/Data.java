package com.smartit.jobSeeker.apiResponse.inviationModule;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("invitation_list")
    @Expose
    private List<InvitationList> invitationList = null;

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("remaining")
    @Expose
    private Integer remaining;

    public List<InvitationList> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(List<InvitationList> invitationList) {
        this.invitationList = invitationList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

}