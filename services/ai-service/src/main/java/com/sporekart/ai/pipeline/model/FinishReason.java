package com.sporekart.ai.pipeline.model;

public enum FinishReason {
    STOP,
    LENGTH,
    CONTENT_FILTER,
    TOOL_CALLS,
    FUNCTION_CALL,
    ERROR,
    TIMEOUT,
    CANCELLED,
    UNKNOWN
}
