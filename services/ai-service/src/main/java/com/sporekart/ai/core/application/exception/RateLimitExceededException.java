package com.sporekart.ai.core.application.exception;

public class RateLimitExceededException extends AiCoreException {
    public RateLimitExceededException(String module) {
        super("Rate limit exceeded for AI module [" + module + "]");
    }
}
