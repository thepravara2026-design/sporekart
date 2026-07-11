package com.sporekart.ai.common.exception;

public class ConversationLimitException extends RuntimeException {
    public ConversationLimitException(String message) {
        super(message);
    }
}

