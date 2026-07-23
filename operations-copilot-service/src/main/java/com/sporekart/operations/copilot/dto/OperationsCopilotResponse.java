package com.sporekart.operations.copilot.dto;

public record OperationsCopilotResponse<T>(
    boolean success,
    String message,
    T data,
    String errorCode
) {
    public static <T> OperationsCopilotResponse<T> success(T data) {
        return new OperationsCopilotResponse<>(true, "Success", data, null);
    }
    public static <T> OperationsCopilotResponse<T> success(String message, T data) {
        return new OperationsCopilotResponse<>(true, message, data, null);
    }
    public static <T> OperationsCopilotResponse<T> error(String message, String errorCode) {
        return new OperationsCopilotResponse<>(false, message, null, errorCode);
    }
}
