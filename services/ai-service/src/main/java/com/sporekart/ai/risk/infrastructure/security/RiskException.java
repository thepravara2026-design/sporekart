package com.sporekart.ai.risk.infrastructure.security;

public class RiskException extends RuntimeException {

    private final String code;
    private final String message;

    public RiskException(String code, String message) {
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

    public static RiskException notFound(String message) {
        return new RiskException("RSK_404", message);
    }

    public static RiskException badRequest(String message) {
        return new RiskException("RSK_400", message);
    }

    public static RiskException forbidden(String message) {
        return new RiskException("RSK_403", message);
    }

    public static RiskException conflict(String message) {
        return new RiskException("RSK_409", message);
    }
}
