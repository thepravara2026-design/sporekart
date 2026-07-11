package com.sporekart.ai.gateway.domain;

import java.time.OffsetDateTime;

public record AIRequestMetadata(
        String source,
        String clientIp,
        String userAgent,
        String apiVersion,
        OffsetDateTime receivedAt) {
}
