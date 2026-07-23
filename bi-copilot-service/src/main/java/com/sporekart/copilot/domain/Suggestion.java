package com.sporekart.copilot.domain;

public record Suggestion(
    String id,
    String text,
    String type,
    String action
) {}
