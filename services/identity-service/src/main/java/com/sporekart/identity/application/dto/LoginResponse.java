package com.sporekart.identity.application.dto;

import java.util.List;
import java.util.Map;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        String sessionId,
        String userId,
        List<String> roles,
        List<String> permissions,
        boolean emailVerified,
        boolean phoneVerified,
        Map<String, Object> workspace) {}
