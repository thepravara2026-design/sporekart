package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AssistantResponse {
    @Schema(description = "Assistant type", example = "customer")
    private String assistant;

    @Schema(description = "Assistant response text")
    private String response;

    public AssistantResponse() {
    }

    public AssistantResponse(String assistant, String response) {
        this.assistant = assistant;
        this.response = response;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
