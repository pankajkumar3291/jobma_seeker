package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses.functional_area;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FunctionalAreaData {

    private  boolean ischecked=false;

    public boolean isIschecked(){
        return ischecked;
    }
    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
