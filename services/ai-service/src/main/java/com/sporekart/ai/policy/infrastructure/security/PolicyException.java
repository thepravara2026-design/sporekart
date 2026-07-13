package com.sporekart.ai.policy.infrastructure.security;

public class PolicyException extends RuntimeException {
    private final String code;
    private final int status;

    public PolicyException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() { return code; }
    public int getStatus() { return status; }

    public static PolicyException notFound(String message) {
        return new PolicyException("POL_404", message, 404);
    }

    public static PolicyException badRequest(String message) {
        return new PolicyException("POL_400", message, 400);
    }

    public static PolicyException evaluationFailed(String message) {
        return new PolicyException("POL_500", message, 500);
    }
}
