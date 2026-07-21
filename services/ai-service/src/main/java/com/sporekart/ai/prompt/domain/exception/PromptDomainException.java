package com.sporekart.ai.prompt.domain.exception;

public class PromptDomainException extends RuntimeException {
    public PromptDomainException(String message) { super(message); }
    public PromptDomainException(String message, Throwable cause) { super(message, cause); }
}
