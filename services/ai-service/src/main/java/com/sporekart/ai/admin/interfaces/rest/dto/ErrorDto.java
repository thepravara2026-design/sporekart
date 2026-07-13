package com.sporekart.ai.admin.interfaces.rest.dto;

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
}
