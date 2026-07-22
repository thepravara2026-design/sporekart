package com.sporekart.ai.conversation.domain;

public record Citation(String sourceId, String sourceType, String snippet, Integer relevanceScore) {
}
