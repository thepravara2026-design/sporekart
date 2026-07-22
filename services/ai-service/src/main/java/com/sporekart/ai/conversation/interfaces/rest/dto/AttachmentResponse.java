package com.sporekart.ai.conversation.interfaces.rest.dto;

public class AttachmentResponse {
    private final String name;
    private final String type;
    private final String url;
    private final Long size;

    public AttachmentResponse(String name, String type, String url, Long size) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Long getSize() {
        return size;
    }
}
