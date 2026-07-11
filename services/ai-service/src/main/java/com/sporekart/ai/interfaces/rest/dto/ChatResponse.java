package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChatResponse {
    @Schema(description = "Chat response text")
    private String response;

    public ChatResponse() {
    }

    public ChatResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
