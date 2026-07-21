package com.sporekart.ai.runtime.application;

public class AgentRuntimeException extends RuntimeException {
    public AgentRuntimeException(String message) {
        super(message);
    }

    public AgentRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
