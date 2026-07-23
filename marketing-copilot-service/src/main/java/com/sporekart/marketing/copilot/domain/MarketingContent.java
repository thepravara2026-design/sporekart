package com.sporekart.marketing.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record MarketingContent(
    String id,
    String title,
    String body,
    ContentType type,
    ContentStatus status,
    String targetAudience,
    List<String> keywords,
    String locale,
    String seoMetadata,
    String brandVoice,
    LocalDateTime createdAt,
    LocalDateTime scheduledAt,
    LocalDateTime publishedAt,
    String createdBy,
    String campaignId
) {
    public enum ContentType {
        BLOG_ARTICLE, PRODUCT_DESCRIPTION, LANDING_PAGE, SOCIAL_POST,
        EMAIL, WHATSAPP_MESSAGE, AD_COPY, PRESS_RELEASE, NEWSLETTER
    }

    public enum ContentStatus {
        DRAFT, REVIEW, APPROVED, SCHEDULED, PUBLISHED, ARCHIVED
    }
}
