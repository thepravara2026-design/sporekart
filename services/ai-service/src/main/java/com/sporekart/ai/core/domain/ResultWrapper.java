package com.sporekart.ai.core.domain;

import java.util.Optional;
import java.util.function.Function;

public final class ResultWrapper<T> {
    private final T data;
    private final String errorCode;
    private final String errorMessage;
    private final boolean success;

    private ResultWrapper(T data) {
        this.data = data;
        this.errorCode = null;
        this.errorMessage = null;
        this.success = true;
    }

    private ResultWrapper(String errorCode, String errorMessage) {
        this.data = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = false;
    }

    public static <T> ResultWrapper<T> success(T data) {
        return new ResultWrapper<>(data);
    }

    public static <T> ResultWrapper<T> failure(String errorCode, String errorMessage) {
        return new ResultWrapper<>(errorCode, errorMessage);
    }

    public boolean isSuccess() { return success; }
    public Optional<T> getData() { return Optional.ofNullable(data); }
    public T getOrThrow() { if (!success) throw new RuntimeException(errorMessage); return data; }
    public <R> ResultWrapper<R> map(Function<T, R> mapper) {
        if (success) return success(mapper.apply(data));
        return failure(errorCode, errorMessage);
    }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
