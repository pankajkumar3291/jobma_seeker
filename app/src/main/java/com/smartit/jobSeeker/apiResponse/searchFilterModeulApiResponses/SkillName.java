package com.smartit.jobSeeker.apiResponse.searchFilterModeulApiResponses;
public class SkillName {

    private String skillName;
    private boolean isChecked;


    public SkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
