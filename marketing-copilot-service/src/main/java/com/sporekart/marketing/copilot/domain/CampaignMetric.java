package com.sporekart.marketing.copilot.domain;

import java.time.LocalDate;

public record CampaignMetric(
    LocalDate date,
    int impressions,
    int clicks,
    int conversions,
    double spend,
    double revenue,
    double ctr,
    double conversionRate,
    double roas
) {}
