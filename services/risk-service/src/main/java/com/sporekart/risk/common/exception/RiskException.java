package com.sporekart.risk.common.exception;

public abstract class RiskException extends RuntimeException {
    protected RiskException(String message) {
        super(message);
    }
}