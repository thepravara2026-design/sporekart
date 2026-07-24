package com.sporekart.platform.util;

import java.util.UUID;

public class IdGenerator {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String shortId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public static String prefixedId(String prefix) {
        return prefix + "_" + shortId();
    }

    public static String sequencedId(String prefix, long sequence) {
        return String.format("%s_%06d", prefix, sequence);
    }
}
