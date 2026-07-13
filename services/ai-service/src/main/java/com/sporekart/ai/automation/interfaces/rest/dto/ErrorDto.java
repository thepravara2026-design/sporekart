package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record ErrorDto(
    String type,
    String title,
    int status,
    String detail,
    Map<String, Object> extensions
) {
    public static ErrorDto of(int status, String title, String detail) {
        return new ErrorDto("about:blank", title, status, detail, Map.of());
    }

    public static ErrorDto of(String type, int status, String title, String detail) {
        return new ErrorDto(type, title, status, detail, Map.of());
    }

    public static ErrorDto of(int status, String title, String detail, Map<String, Object> extensions) {
        return new ErrorDto("about:blank", title, status, detail, extensions);
    }
}
