package com.sporekart.ai.compliance.infrastructure.security;

public class ComplianceException extends RuntimeException {

    private final String errorCode;
    private final int httpStatus;

    public ComplianceException(String errorCode, int httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ComplianceException(String errorCode, int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static ComplianceException notFound(String message) {
        return new ComplianceException("CMP_404", 404, message);
    }

    public static ComplianceException badRequest(String message) {
        return new ComplianceException("CMP_400", 400, message);
    }

    public static ComplianceException forbidden(String message) {
        return new ComplianceException("CMP_403", 403, message);
    }

    public static ComplianceException conflict(String message) {
        return new ComplianceException("CMP_409", 409, message);
    }
}
