package com.sporekart.gateway.error;

import org.springframework.http.HttpStatus;

public class GatewayException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;

    public GatewayException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() { return httpStatus; }
    public String getErrorCode() { return errorCode; }
}
