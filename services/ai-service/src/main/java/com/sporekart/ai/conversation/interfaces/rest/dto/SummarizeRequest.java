package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.util.List;

public class SummarizeRequest {
    private List<MessageRequest> messages;

    public SummarizeRequest() {
    }

    public List<MessageRequest> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageRequest> messages) {
        this.messages = messages;
    }
}
