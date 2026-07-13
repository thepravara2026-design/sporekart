package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.Map;

public record PolicyErrorDto(String type, String title, int status, String detail, Map<String, Object> extensions) {
    public static PolicyErrorDto of(int status, String title, String detail) {
        return new PolicyErrorDto("about:blank", title, status, detail, Map.of());
    }
}
