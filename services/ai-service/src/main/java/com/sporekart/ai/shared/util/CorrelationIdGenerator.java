package com.sporekart.ai.shared.util;

import java.util.UUID;

public final class CorrelationIdGenerator {

    private CorrelationIdGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static String from(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }
}
