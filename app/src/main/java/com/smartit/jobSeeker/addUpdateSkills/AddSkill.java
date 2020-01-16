package com.smartit.jobSeeker.addUpdateSkills;

import java.io.Serializable;

public class AddSkill implements Serializable{

    private String updateSkills;

    public AddSkill(String updateSkills) {

        this.updateSkills = updateSkills;
    }

    public String getUpdateSkills() {
        return updateSkills;
    }

    public void setUpdateSkills(String updateSkills) {
        this.updateSkills = updateSkills;
    }


}
