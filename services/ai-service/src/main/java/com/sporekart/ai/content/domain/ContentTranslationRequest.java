package com.sporekart.ai.content.domain;

import java.util.Map;
import java.util.UUID;

public record ContentTranslationRequest(
    UUID id,
    String sourceText,
    String sourceLanguage,
    String targetLanguage,
    boolean preserveFormatting,
    Map<String, String> glossary) {}
