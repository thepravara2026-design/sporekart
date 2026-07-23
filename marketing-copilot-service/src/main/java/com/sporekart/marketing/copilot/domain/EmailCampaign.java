package com.sporekart.marketing.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record EmailCampaign(
    String id,
    String subject,
    String preheader,
    String bodyHtml,
    String bodyText,
    List<String> segments,
    EmailType type,
    String senderName,
    String senderEmail,
    String replyTo,
    LocalDateTime scheduledAt,
    List<String> contentBlocks,
    String campaignId
) {
    public enum EmailType {
        PROMOTIONAL, TRANSACTIONAL, NEWSLETTER, ABANDONED_CART,
        WELCOME, REENGAGEMENT, EDUCATIONAL, EVENT
    }
}
