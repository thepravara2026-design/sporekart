package com.sporekart.executive.copilot.dto;

public record ExecutiveCopilotResponse<T>(
    boolean success,
    String message,
    T data,
    String errorCode
) {
    public static <T> ExecutiveCopilotResponse<T> success(T data) {
        return new ExecutiveCopilotResponse<>(true, "Success", data, null);
    }
    public static <T> ExecutiveCopilotResponse<T> success(String message, T data) {
        return new ExecutiveCopilotResponse<>(true, message, data, null);
    }
    public static <T> ExecutiveCopilotResponse<T> error(String message, String errorCode) {
        return new ExecutiveCopilotResponse<>(false, message, null, errorCode);
    }
}
