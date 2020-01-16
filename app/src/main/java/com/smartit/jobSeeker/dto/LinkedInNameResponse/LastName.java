package com.smartit.jobSeeker.dto.LinkedInNameResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastName {
    @Expose
    @SerializedName("preferredLocale")
    private PreferredLocale preferredLocale;
    @Expose
    @SerializedName("localized")
    private Localized localized;

    public PreferredLocale getPreferredLocale() {
        return preferredLocale;
    }

    public void setPreferredLocale(PreferredLocale preferredLocale) {
        this.preferredLocale = preferredLocale;
    }

    public Localized getLocalized() {
        return localized;
    }

    public void setLocalized(Localized localized) {
        this.localized = localized;
    }
}
