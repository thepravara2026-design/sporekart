package com.sporekart.marketing.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record WhatsAppMessage(
    String id,
    String templateName,
    List<String> templateParameters,
    String body,
    String mediaUrl,
    MessageType type,
    String recipientSegment,
    LocalDateTime scheduledAt,
    String campaignId
) {
    public enum MessageType {
        TEXT, IMAGE, VIDEO, DOCUMENT, INTERACTIVE, TEMPLATE
    }
}
