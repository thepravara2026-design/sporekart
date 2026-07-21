package com.sporekart.ai.shared.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class IdempotencyKeyGenerator {

    private IdempotencyKeyGenerator() {
    }

    public static String generate(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            return CorrelationIdGenerator.generate();
        }
    }
}
