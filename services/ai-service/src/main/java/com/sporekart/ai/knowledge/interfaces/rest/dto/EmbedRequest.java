package com.sporekart.ai.knowledge.interfaces.rest.dto;

public class EmbedRequest {
    private String text;
    private String provider;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
