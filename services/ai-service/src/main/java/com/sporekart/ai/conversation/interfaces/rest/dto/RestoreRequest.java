package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.util.List;

public class RestoreRequest {
    private String summaryId;
    private List<MessageRequest> recentMessages;

    public RestoreRequest() {
    }

    public String getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(String summaryId) {
        this.summaryId = summaryId;
    }

    public List<MessageRequest> getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(List<MessageRequest> recentMessages) {
        this.recentMessages = recentMessages;
    }
}
