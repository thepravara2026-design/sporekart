package com.sporekart.ai.core.application.validation;

import com.sporekart.ai.core.domain.AiConstants;
import com.sporekart.ai.core.domain.AiErrorCode;
import com.sporekart.ai.core.application.exception.AiCoreException;
import java.util.Collection;
import java.util.Map;

public final class AiValidationUtils {
    private AiValidationUtils() {}

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new AiCoreException(AiErrorCode.VALIDATION_ERROR.getCode() + ": " + fieldName + " must not be blank") {};
        }
    }

    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new AiCoreException(AiErrorCode.VALIDATION_ERROR.getCode() + ": " + fieldName + " must not be null") {};
        }
    }

    public static void requireMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new AiCoreException(AiErrorCode.VALIDATION_ERROR.getCode() + ": " + fieldName + " exceeds max length of " + maxLength) {};
        }
    }

    public static void validatePromptLength(String prompt) {
        if (prompt != null && prompt.length() > AiConstants.MAX_PROMPT_LENGTH) {
            throw new AiCoreException(AiErrorCode.PROMPT_TOO_LONG.getCode() + ": Prompt exceeds " + AiConstants.MAX_PROMPT_LENGTH + " characters") {};
        }
    }

    public static void requireNotEmpty(Collection<?> collection, String fieldName) {
        if (collection == null || collection.isEmpty()) {
            throw new AiCoreException(AiErrorCode.VALIDATION_ERROR.getCode() + ": " + fieldName + " must not be empty") {};
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new AiCoreException(AiErrorCode.VALIDATION_ERROR.getCode() + ": " + fieldName + " must be positive") {};
        }
    }
}
