package com.sporekart.ai.core.domain;

public enum AiErrorCode {
    PLATFORM_DISABLED("AI-001", "AI platform is disabled", 503),
    MODULE_DISABLED("AI-002", "AI module is disabled", 503),
    PROVIDER_NOT_AVAILABLE("AI-003", "AI provider not available", 503),
    PROVIDER_TIMEOUT("AI-004", "AI provider request timed out", 504),
    RATE_LIMIT_EXCEEDED("AI-005", "Rate limit exceeded", 429),
    INVALID_REQUEST("AI-006", "Invalid AI request", 400),
    VALIDATION_ERROR("AI-007", "Validation error", 400),
    PROMPT_TOO_LONG("AI-008", "Prompt exceeds maximum length", 400),
    CONTENT_FILTERED("AI-009", "Content filtered by safety policy", 422),
    UNSUPPORTED_MODEL("AI-010", "Unsupported model", 400),
    CONVERSATION_NOT_FOUND("AI-011", "Conversation not found", 404),
    KNOWLEDGE_NOT_FOUND("AI-012", "Knowledge document not found", 404),
    EMBEDDING_FAILED("AI-013", "Embedding generation failed", 500),
    CONTENT_GENERATION_FAILED("AI-014", "Content generation failed", 500),
    WORKFLOW_EXECUTION_FAILED("AI-015", "Workflow execution failed", 500),
    INTERNAL_ERROR("AI-999", "Internal AI platform error", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    AiErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public int getHttpStatus() { return httpStatus; }
}
