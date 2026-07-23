package com.sporekart.marketing.copilot.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CampaignResponse(
    String campaignId,
    String name,
    String type,
    String status,
    String objective,
    LocalDate startDate,
    LocalDate endDate,
    double budget,
    List<String> channels,
    List<String> contentIds,
    List<String> recommendations,
    LocalDateTime createdAt
) {}
