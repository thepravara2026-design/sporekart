package com.sporekart.marketing.copilot.dto;

public record WhatsAppResponse(
    String messageId,
    String templateName,
    String body,
    String messageType,
    String recipientSegment
) {}
