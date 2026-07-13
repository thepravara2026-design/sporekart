package com.sporekart.ai.admin.infrastructure.security;

public class AdminException extends RuntimeException {

    private final String code;

    public AdminException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AdminException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AdminException notFound(String message) {
        return new AdminException("ADM_404", message);
    }

    public static AdminException badRequest(String message) {
        return new AdminException("ADM_400", message);
    }

    public static AdminException forbidden(String message) {
        return new AdminException("ADM_403", message);
    }

    public static AdminException conflict(String message) {
        return new AdminException("ADM_409", message);
    }
}
