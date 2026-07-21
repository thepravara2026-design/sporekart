package com.sporekart.ai.shared.util;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isValidPrompt(String prompt) {
        return prompt != null && !prompt.isBlank() && prompt.length() <= 32000;
    }

    public static boolean isValidInput(String input) {
        return input != null && !input.isBlank() && input.length() <= 64000;
    }

    public static boolean isValidId(String id) {
        return id != null && !id.isBlank();
    }
}
