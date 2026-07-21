package com.sporekart.ai.shared.constants;

public final class AiConstants {

    private AiConstants() {
    }

    public static final String API_BASE_PATH = "/api/v1/ai";
    public static final String INTERNAL_API_PATH = "/api/v1/internal/ai";

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CAUSATION_ID_HEADER = "X-Causation-Id";
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String AGENT_ID_HEADER = "X-Agent-Id";
    public static final String IDEMPOTENCY_KEY_HEADER = "X-Idempotency-Key";

    public static final int MAX_PROMPT_LENGTH = 32000;
    public static final int MAX_INPUT_LENGTH = 64000;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEFAULT_TIMEZONE = "UTC";

    public static final String CACHE_GATEWAY = "ai:gateway:";
    public static final String CACHE_PROVIDER = "ai:provider:";
    public static final String CACHE_PROMPT = "ai:prompt:";
    public static final String CACHE_MEMORY = "ai:memory:";
    public static final String CACHE_RUNTIME = "ai:runtime:";
    public static final String CACHE_SEMANTIC = "ai:semantic:";
    public static final String CACHE_CONVERSATION = "ai:conversation:";
    public static final String CACHE_KNOWLEDGE = "ai:knowledge:";
}
