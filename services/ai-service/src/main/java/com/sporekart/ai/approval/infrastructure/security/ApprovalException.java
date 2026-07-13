package com.sporekart.ai.approval.infrastructure.security;

public class ApprovalException extends RuntimeException {

    private final String code;
    private final String message;

    public ApprovalException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static ApprovalException notFound(String message) {
        return new ApprovalException("APR_404", message);
    }

    public static ApprovalException badRequest(String message) {
        return new ApprovalException("APR_400", message);
    }

    public static ApprovalException forbidden(String message) {
        return new ApprovalException("APR_403", message);
    }

    public static ApprovalException conflict(String message) {
        return new ApprovalException("APR_409", message);
    }
}
