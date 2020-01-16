package com.smartit.jobSeeker.skills;

import java.io.Serializable;

public class Skills implements Serializable {


    private String updateSkills;

    public Skills(String updateSkills) {

        this.updateSkills = updateSkills;
    }

    public String getUpdateSkills() {
        return updateSkills;
    }

    public void setUpdateSkills(String updateSkills) {
        this.updateSkills = updateSkills;
    }


}
