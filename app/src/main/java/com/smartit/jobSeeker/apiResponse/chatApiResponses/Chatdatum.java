package com.smartit.jobSeeker.apiResponse.chatApiResponses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chatdatum {

    @SerializedName("jobma_interaction_id")
    @Expose
    private Integer jobmaInteractionId;
    @SerializedName("jobma_contact_id")
    @Expose
    private Integer jobmaContactId;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("interaction_by")
    @Expose
    private Integer interactionBy;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getJobmaInteractionId() {
        return jobmaInteractionId;
    }

    public void setJobmaInteractionId(Integer jobmaInteractionId) {
        this.jobmaInteractionId = jobmaInteractionId;
    }

    public Integer getJobmaContactId() {
        return jobmaContactId;
    }

    public void setJobmaContactId(Integer jobmaContactId) {
        this.jobmaContactId = jobmaContactId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getInteractionBy() {
        return interactionBy;
    }

    public void setInteractionBy(Integer interactionBy) {
        this.interactionBy = interactionBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}