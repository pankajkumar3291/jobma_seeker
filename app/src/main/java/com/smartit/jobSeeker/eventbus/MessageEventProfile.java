package com.smartit.jobSeeker.eventbus;

public class MessageEventProfile {

    public MessageEventProfile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    private String path;

    /* Additional fields if needed */
}
