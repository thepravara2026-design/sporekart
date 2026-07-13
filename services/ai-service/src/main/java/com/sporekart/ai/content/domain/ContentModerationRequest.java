package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.util.UUID;

public record ContentModerationRequest(
    UUID id,
    String content,
    ContentType contentType,
    boolean checkPii,
    boolean checkProfanity,
    boolean checkToxicity) {}
