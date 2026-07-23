package com.sporekart.customer.copilot.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record CustomerProfile(
    String userId,
    String userName,
    String email,
    String preferredLanguage,
    Map<String, Object> preferences,
    OffsetDateTime joinedAt
) {
    public CustomerProfile {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be blank");
        }
        if (userName == null || userName.isBlank()) {
            userName = "Unknown";
        }
        if (email == null) {
            email = "";
        }
        if (preferredLanguage == null || preferredLanguage.isBlank()) {
            preferredLanguage = "en";
        }
        if (preferences == null) {
            preferences = Map.of();
        }
        if (joinedAt == null) {
            joinedAt = OffsetDateTime.now();
        }
    }
}
