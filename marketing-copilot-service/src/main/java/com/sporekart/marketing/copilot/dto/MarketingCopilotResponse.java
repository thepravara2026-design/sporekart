package com.sporekart.marketing.copilot.dto;

public record MarketingCopilotResponse<T>(
    boolean success,
    String message,
    T data,
    String errorCode
) {
    public static <T> MarketingCopilotResponse<T> success(T data) {
        return new MarketingCopilotResponse<>(true, "Success", data, null);
    }

    public static <T> MarketingCopilotResponse<T> success(String message, T data) {
        return new MarketingCopilotResponse<>(true, message, data, null);
    }

    public static <T> MarketingCopilotResponse<T> error(String message, String errorCode) {
        return new MarketingCopilotResponse<>(false, message, null, errorCode);
    }
}
