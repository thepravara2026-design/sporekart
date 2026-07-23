package com.sporekart.marketing.copilot.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record Campaign(
    String id,
    String name,
    CampaignType type,
    CampaignStatus status,
    String objective,
    String targetAudience,
    LocalDate startDate,
    LocalDate endDate,
    double budget,
    List<String> channels,
    List<String> contentIds,
    List<CampaignMetric> metrics,
    LocalDateTime createdAt,
    String createdBy
) {
    public enum CampaignType {
        LAUNCH, SEASONAL, BRAND_AWARENESS, LEAD_GENERATION, RETENTION,
        REACTIVATION, CROSS_SELL, UPSELL, PROMOTIONAL, EDUCATIONAL
    }

    public enum CampaignStatus {
        PLANNING, ACTIVE, PAUSED, COMPLETED, CANCELLED, ARCHIVED
    }
}
