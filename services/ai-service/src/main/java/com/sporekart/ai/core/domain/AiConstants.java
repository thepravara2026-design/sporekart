package com.sporekart.ai.core.domain;

public final class AiConstants {
    public static final String AI_PLATFORM = "ai-platform";
    public static final String VERSION = "1.0.0";
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String API_VERSION_HEADER = "X-API-Version";
    public static final int DEFAULT_MAX_TOKENS = 2048;
    public static final int DEFAULT_TEMPERATURE = 7;
    public static final int MAX_PROMPT_LENGTH = 32000;
    public static final int MAX_RETRIES = 3;
    public static final long DEFAULT_TIMEOUT_MS = 30000;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String[] SUPPORTED_PROVIDERS = {"GEMINI", "OPENAI", "CLAUDE"};

    private AiConstants() {
    }
}
