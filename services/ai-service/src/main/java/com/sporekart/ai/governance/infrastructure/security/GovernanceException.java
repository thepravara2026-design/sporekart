package com.sporekart.ai.governance.infrastructure.security;

public class GovernanceException extends RuntimeException {
    private final String code;
    private final int status;

    public GovernanceException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public GovernanceException(String code, String message, int status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public static GovernanceException notFound(String message) {
        return new GovernanceException("GOV_404", message, 404);
    }

    public static GovernanceException badRequest(String message) {
        return new GovernanceException("GOV_400", message, 400);
    }

    public static GovernanceException forbidden(String message) {
        return new GovernanceException("GOV_403", message, 403);
    }

    public static GovernanceException internal(String message) {
        return new GovernanceException("GOV_500", message, 500);
    }
}
