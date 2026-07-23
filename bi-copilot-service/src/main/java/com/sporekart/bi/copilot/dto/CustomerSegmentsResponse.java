package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.sporekart.bi.copilot.domain.CustomerSegment;

public record CustomerSegmentsResponse(
    List<CustomerSegment> segments,
    int totalCustomers,
    double totalRevenue,
    OffsetDateTime calculatedAt
) {}
