package com.sporekart.admin.dto;

import com.sporekart.admin.domain.AdminDashboard;
import com.sporekart.admin.domain.BusinessInsight;
import com.sporekart.admin.domain.OperationalAlert;

import java.time.OffsetDateTime;
import java.util.List;

public record DashboardResponse(
    AdminDashboard dashboard,
    List<BusinessInsight> insights,
    List<OperationalAlert> alerts,
    OffsetDateTime timestamp
) {}
