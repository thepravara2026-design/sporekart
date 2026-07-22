package com.sporekart.ai.conversation.interfaces.rest.dto;

public class RestoreResponse {
    private final String restoredContent;
    private final int totalTokens;
    private final int messageCount;

    public RestoreResponse(String restoredContent, int totalTokens, int messageCount) {
        this.restoredContent = restoredContent;
        this.totalTokens = totalTokens;
        this.messageCount = messageCount;
    }

    public String getRestoredContent() {
        return restoredContent;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public int getMessageCount() {
        return messageCount;
    }
}
