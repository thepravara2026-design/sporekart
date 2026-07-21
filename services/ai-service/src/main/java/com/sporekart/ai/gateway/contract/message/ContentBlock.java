package com.sporekart.ai.gateway.contract.message;

import java.util.Map;

public record ContentBlock(
    String type,
    String text,
    Map<String, Object> image,
    Map<String, Object> audio,
    Map<String, Object> toolUse
) {}
