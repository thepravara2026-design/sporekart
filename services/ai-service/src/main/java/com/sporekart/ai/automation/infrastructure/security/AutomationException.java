package com.sporekart.ai.automation.infrastructure.security;

public class AutomationException extends RuntimeException {

    private final String code;

    public AutomationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AutomationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AutomationException notFound(String message) {
        return new AutomationException("AUT_404", message);
    }

    public static AutomationException badRequest(String message) {
        return new AutomationException("AUT_400", message);
    }

    public static AutomationException forbidden(String message) {
        return new AutomationException("AUT_403", message);
    }

    public static AutomationException conflict(String message) {
        return new AutomationException("AUT_409", message);
    }
}
