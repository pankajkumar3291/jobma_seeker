package com.smartit.jobSeeker.activities;

import com.smartit.jobSeeker.dto.LinkedInEmailResponse.LinkedInEmailResponse;
import com.smartit.jobSeeker.dto.LinkedInNameResponse.NameResponse;

class NameAndEmailAddress {

    public NameAndEmailAddress(LinkedInEmailResponse linkedInEmailResponse, NameResponse nameResponse) {
        this.linkedInEmailResponse = linkedInEmailResponse;
        this.nameResponse = nameResponse;
    }

    public LinkedInEmailResponse getLinkedInEmailResponse() {
        return linkedInEmailResponse;
    }

    public NameResponse getNameResponse() {
        return nameResponse;
    }

    private LinkedInEmailResponse linkedInEmailResponse;
    private NameResponse nameResponse;
}
