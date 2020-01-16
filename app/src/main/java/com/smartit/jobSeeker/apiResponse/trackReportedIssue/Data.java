package com.smartit.jobSeeker.apiResponse.trackReportedIssue;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("remaining")
    @Expose
    private Integer remaining;

    @SerializedName("report_list")
    @Expose
    private List<ReportList> reportList = null;

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

    public List<ReportList> getReportList() {
        return reportList;
    }

    public void setReportList(List<ReportList> reportList) {
        this.reportList = reportList;
    }

}
