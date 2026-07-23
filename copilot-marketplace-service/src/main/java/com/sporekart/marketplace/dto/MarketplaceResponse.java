package com.sporekart.marketplace.dto;

public record MarketplaceResponse<T>(
    boolean success,
    String message,
    T data,
    String errorCode
) {
    public static <T> MarketplaceResponse<T> success(T data) {
        return new MarketplaceResponse<>(true, "Success", data, null);
    }
    public static <T> MarketplaceResponse<T> success(String message, T data) {
        return new MarketplaceResponse<>(true, message, data, null);
    }
    public static <T> MarketplaceResponse<T> error(String message, String errorCode) {
        return new MarketplaceResponse<>(false, message, null, errorCode);
    }
}